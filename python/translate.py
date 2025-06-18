import openai
import os
import re
import json
from time import sleep
from dotenv import load_dotenv

target_language = "Japanese"
target_filename = "../src/main/resources/org/sonar/l10n/core_ja.properties"
source_filename = "../target/surefire-reports/org.sonar.plugins.l10n.JapanesePackPluginTest.txt"

 # Set your API key securely (env var preferred)
load_dotenv()
openai_api_key = os.environ.get("OPENAI_API_KEY")
openai_model = os.environ.get("OPENAI_MODEL", "gpt-4o")
client = openai.OpenAI(api_key=openai_api_key)

def load_properties(file_path):
    props = {}
    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            if line.strip() and not line.startswith("#") and "=" in line:
                key, value = line.strip().split("=", 1)
                props[key.strip()] = value.strip()
    return props

def save_properties(props, file_path):
    with open(file_path, 'w', encoding='utf-8') as f:
        for key, value in props.items():
            f.write(f"{key}={value}\n")

def chunk_dict(d, chunk_size):
    items = list(d.items())
    for i in range(0, len(items), chunk_size):
        yield dict(items[i:i + chunk_size])

def translate_chunk(chunk, target_lang="Korean"):
    keys = list(chunk.keys())
    values = list(chunk.values())

    input_text = "\n".join(
        f"{i+1}. {v}" for i, v in enumerate(values)
    )

    prompt = (
        f"You are a technical writer for SonarQube. Translate the following English lines into {target_lang}. "
        f"Return only the translated lines, numbered the same:\n\n{input_text}"
    )

    try:
        response = client.chat.completions.create(
            model="gpt-4o",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.3,
        )
        translated_lines = response.choices[0].message.content.strip().splitlines()
    except Exception as e:
        print(f"Error during translation: {e}")
        return {}

    translated = {}
    for line in translated_lines:
        if ". " in line:
            try:
                idx_str, translated_text = line.split(". ", 1)
                idx = int(idx_str) - 1
                translated[keys[idx]] = translated_text.strip()
            except Exception:
                continue

    return translated

def extract_expected_translation(input_filename):
    start_marker = " Missing translations are:"
    end_marker = "See report file located at:"
    pattern = re.escape(start_marker) + r"(.*?)" + re.escape(end_marker)

    try:
        with open(input_filename, 'r', encoding='utf-8') as f:
            full_text = f.read()
    except FileNotFoundError:
        print(f"Error: The file '{input_filename}' was not found.")
        return
    except Exception as e:
        print(f"An error occurred while reading the file: {e}")
        return

    match = re.search(pattern, full_text, re.DOTALL)

    if match:
        # The desired text is in the first capturing group.
        # .strip() is used to remove leading/trailing whitespace.
        return match.group(1).strip()
    else:
        # Return None if no match is found
        return None

def parse_properties(properties_string):
    properties_dict = {}
    # Split the multi-line string into individual lines
    for line in properties_string.splitlines():
        # Clean up whitespace from the start and end of the line
        line = line.strip()
        # Ignore empty lines or lines that are comments (starting with # or !)
        if not line or line.startswith('#') or line.startswith('!'):
            continue

        # Split the line into a key and a value at the first '='
        if '=' in line:
            key, value = line.split('=', 1)
            # .strip() cleans up any extra space around the key and value
            properties_dict[key.strip()] = value.strip()

    return properties_dict

def translate_properties(input_file, output_file, target_lang="Korean", batch_size=20):

    props = parse_properties(extract_expected_translation(input_file))
    translated_all = {}

    print(f"Translating {len(props)} entries in batches of {batch_size}...")
    for i, chunk in enumerate(chunk_dict(props, batch_size), 1):
        print(f"🔄 Translating batch {i}...")
        translated = translate_chunk(chunk, target_lang)
        translated_all.update(translated)
        sleep(1)  # optional: avoid rate limit

    save_properties(translated_all, output_file)
    print(f"✅ Translation complete. Output saved to {output_file}")

# Example usage
translate_properties(source_filename, target_filename, target_language, 500)
