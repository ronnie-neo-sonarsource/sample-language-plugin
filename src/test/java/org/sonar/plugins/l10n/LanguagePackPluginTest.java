/*
 * L10n :: Language Pack
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.l10n;

import org.junit.Test;
import org.junit.Assert;
import org.sonar.api.internal.PluginContextImpl;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;
import org.sonar.test.i18n.I18nMatchers;

public class LanguagePackPluginTest {

    @Test
    public void bundles_should_be_up_to_date() {
        // Validates every core_<locale>.properties bundle on the classpath
        // (core_ja.properties and core_ko.properties) against the English core bundle.
        // https://github.com/SonarSource/sonarqube/commit/50c03de3431007269b0966a8fdf1fe032c9521f6
        I18nMatchers.assertBundlesUpToDate();
    }
    // coverage
    @Test
    public void testLanguagePackPlugin() {
        LanguagePackPlugin languagePackPlugin = new LanguagePackPlugin();

        String pluginName = languagePackPlugin.toString();
        Assert.assertEquals("LanguagePackPlugin", pluginName);

        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(10, 8),
                SonarQubeSide.SCANNER, SonarEdition.ENTERPRISE);
        Plugin.Context context = new PluginContextImpl.Builder().setSonarRuntime(runtime).build();
        languagePackPlugin.define(context);
    }

    @Test
    public void has_bundle_should_find_shipped_locales() {
        LanguagePackPlugin languagePackPlugin = new LanguagePackPlugin();

        Assert.assertTrue(languagePackPlugin.has_bundle("ja"));
        Assert.assertTrue(languagePackPlugin.has_bundle("ko"));
        Assert.assertFalse(languagePackPlugin.has_bundle("does_not_exist"));
    }

    @Test
    public void has_all_bundles_should_be_true_when_all_shipped_bundles_are_present() {
        LanguagePackPlugin languagePackPlugin = new LanguagePackPlugin();

        Assert.assertTrue(languagePackPlugin.has_all_bundles());
    }

}
