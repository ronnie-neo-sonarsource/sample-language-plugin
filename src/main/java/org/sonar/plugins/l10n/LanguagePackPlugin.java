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
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void define(Context context) {
        context.addExtensions(Collections.emptyList());
    }

}
