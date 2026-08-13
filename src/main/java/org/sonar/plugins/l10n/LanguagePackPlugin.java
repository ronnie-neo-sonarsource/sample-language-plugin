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

import java.util.Collections;
import java.util.List;

import org.sonar.api.Plugin;

/**
 * Language pack for SonarQube.
 *
 * <p>SonarQube discovers translation bundles purely by their presence on the
 * classpath: every {@code org/sonar/l10n/core_<locale>.properties} file is
 * loaded automatically, keyed by its locale suffix. This plugin ships the
 * Japanese ({@code core_ja.properties}) and Korean ({@code core_ko.properties})
 * bundles, so no extensions need to be registered here.
 */
public final class LanguagePackPlugin implements Plugin {

    private static final String BUNDLE_PATH_FORMAT = "org/sonar/l10n/core_%s.properties";

    /** Locales whose translation bundles this plugin is expected to ship. */
    private static final List<String> BUNDLED_LOCALES = List.of("ja", "ko");

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * Checks whether the translation bundle for a given locale is present on the classpath.
     *
     * @param locale the locale suffix of the bundle, e.g. {@code ja}
     * @return {@code true} if {@code org/sonar/l10n/core_<locale>.properties} can be resolved
     */
    public boolean has_bundle(String locale) {
        String path = String.format(BUNDLE_PATH_FORMAT, locale);
        return getClass().getClassLoader().getResource(path) != null;
    }

    /**
     * Checks whether every bundle this plugin ships is present on the classpath. Useful as a
     * sanity check that the resources were packaged into the jar alongside this class.
     *
     * @return {@code true} if all of {@link #BUNDLED_LOCALES} resolve to a properties file
     */
    public boolean has_all_bundles() {
        return BUNDLED_LOCALES.stream().allMatch(this::has_bundle);
    }

    @Override
    public void define(Context context) {
        // This plugin does not register any extensions, but we still need to call addExtensions()
        // to avoid a NPE in the PluginDefinition class. Run the bundle sanity check first so
        // packaging issues surface early if the shipped resources are missing from the classpath.
        if (!has_all_bundles()) {
            throw new IllegalStateException("Missing translation bundle(s) for locales: " + BUNDLED_LOCALES);
        }
        context.addExtensions(Collections.emptyList());
    }

}
