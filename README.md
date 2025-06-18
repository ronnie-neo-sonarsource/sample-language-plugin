Sample Translation Plugin for SonarQube
==============================

Please also refer to the [SonarQube documentation](https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/internationalization/) for more details on how to create a translation plugin.

These are the steps to generate a translation plugin for SonarQube.
1. Download the source code from the repository:
2. Empty the `src/main/resources/l10n/<TRANSLATION>.properties` file.
3. Build the project using Maven:
   ```bash
   mvn test
   ```
4. This will generate a list of missing translations in the `target/` directory.
5. Configure the python script to include your OpenAI API key by editing the `.env` file in the python folder:
6. Run the python script `generate_translations.py`.  
   ```bash
   python3 generate_translations.py
   ```
   This script will automatically assume the missing translation file in `../target/surefire-reports/org.sonar.plugins.l10n.JapanesePackPluginTest.txt` and output it to `../src/main/resources/org/sonar/l10n/core_ja.properties`
7. Build the project again using Maven:
   ```bash
   mvn clean package
   ```
8. Copy the target JAR file to the SonarQube plugins directory:
   ```bash
   cp target/sonar-l10n-ja-plugin-*.jar /path/to/sonarqube/extensions/plugins/
   ```
   
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