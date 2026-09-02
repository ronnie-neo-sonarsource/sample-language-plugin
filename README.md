Sample Translation Plugin for SonarQube
==============================

Please also refer to the [SonarQube documentation](https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/internationalization/) for more details on how to create a translation plugin.

This pack ships more than one language in a single plugin. SonarQube discovers
translation bundles purely by their presence on the classpath — every
`src/main/resources/org/sonar/l10n/core_<locale>.properties` file is loaded
automatically, keyed by its locale suffix. Today it ships **Japanese**
(`core_ja.properties`) and **Korean** (`core_ko.properties`); to add another
language, drop in a `core_<locale>.properties` file and add the language to the
`LANGUAGES` list in `python/translate.py`. No Java changes are needed.

These are the steps to generate translations for SonarQube.
1. Download the source code from the repository:
2. Empty the `src/main/resources/org/sonar/l10n/core_<locale>.properties` file(s) you want to (re)generate.
3. Build the project using Maven:
   ```bash
   mvn test
   ```
4. This writes a per-locale missing-translation report to `target/l10n/core_<locale>.properties.report.txt`.
5. Configure the python script to include your OpenAI API key by editing the `.env` file in the python folder:
6. Run the python script.
   ```bash
   cd python
   python3 translate.py
   ```
   For each language in its `LANGUAGES` list, the script reads
   `../target/l10n/core_<locale>.properties.report.txt` and writes the
   translations to `../src/main/resources/org/sonar/l10n/core_<locale>.properties`.
7. Build the project again using Maven:
   ```bash
   mvn clean package
   ```
8. Copy the target JAR file to the SonarQube plugins directory:
   ```bash
   cp target/sonar-l10n-plugin-*.jar /path/to/sonarqube/extensions/plugins/
   ```

Maintaining the Plugin
----------------------
You will need to repackage this plugin to release any updates
1. Update your server version in your `pom.xml`
2. Run ``mvn test``

For enterprise and developer edition:

You can also view the full translation file in our public repo here
https://github.com/SonarSource/sonarqube/blob/master/README.md#translations-files

From the ts file, you can convert to `.properties` using the above-mentioned command

Potential Issues
-----------------
You may encounter the following issues with `alerts.operator.!=` not being translated correctly.
You can fix this by updating it to `alerts.operator.!\u003d=`

The same will happen for `alerts.operator.==` which should be updated to `alerts.operator.\u003d=`

The python script assume the following:
- An empty translation file, so if you have existing translations, you may need to manually merge them with the generated translations.
- The source file is in the format of surefire reports missing translations, which is the output of the SonarQube test.


Cost of Utilizing OpenAI
-----------------------------
It will consume approximately 55k tokens to translate. With chatgpt-4o, it will cost approximately US$1. Adjust the model accordingly to fit your budget.

Usage
-----------------
This sample project is free to use for any purpose, including commercial use, without any restrictions. This is not part of Sonar's official plugins. This sample plugin is to be use at your own risk, and it is not supported by Sonar. The plugin is provided "as is" without any warranty of any kind, either express or implied.