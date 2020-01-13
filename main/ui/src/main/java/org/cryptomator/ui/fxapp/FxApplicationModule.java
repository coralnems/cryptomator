/*******************************************************************************
 * Copyright (c) 2017 Skymatic UG (haftungsbeschränkt).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE file.
 *******************************************************************************/
package org.cryptomator.ui.fxapp;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.apache.commons.lang3.SystemUtils;
import org.cryptomator.common.vaults.Vault;
import org.cryptomator.ui.mainwindow.MainWindowComponent;
import org.cryptomator.ui.preferences.PreferencesComponent;
import org.cryptomator.ui.quit.QuitComponent;
import org.cryptomator.ui.unlock.UnlockComponent;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Module(includes = {UpdateCheckerModule.class}, subcomponents = {MainWindowComponent.class, PreferencesComponent.class, UnlockComponent.class, QuitComponent.class})
abstract class FxApplicationModule {

	@Provides
	@FxApplicationScoped
	static ObjectProperty<Vault> provideSelectedVault() {
		return new SimpleObjectProperty<>();
	}

	@Provides
	@Named("windowIcons")
	@FxApplicationScoped
	static List<Image> provideWindowIcons() {
		if (SystemUtils.IS_OS_MAC) {
			return Collections.emptyList();
		}

		try {
			return List.of(createImageFromResource("/window_icon_32.png"), createImageFromResource("/window_icon_512.png"));
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	private static Image createImageFromResource(String resourceName) throws IOException {
		try (InputStream in = FxApplicationModule.class.getResourceAsStream(resourceName)) {
			return new Image(in);
		}
	}

	@Binds
	abstract Application bindApplication(FxApplication application);

	@Provides
	static MainWindowComponent provideMainWindowComponent(MainWindowComponent.Builder builder) {
		return builder.build();
	}

	@Provides
	static PreferencesComponent providePreferencesComponent(PreferencesComponent.Builder builder) {
		return builder.build();
	}

}
