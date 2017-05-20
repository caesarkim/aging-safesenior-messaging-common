/**
 * aging-safesenior-messaging
 */
package com.sfsn.backend.messaging.config;

import java.util.ResourceBundle;

/**
 *  Copyright (C) Projecteria LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jungwhan Kim Projecteria LLC, 2016 April 19
 */

/**
 * @author jungwhan
 * MessagingConfiguration.java
 * 10:56:03 AM May 3, 2016 2016
 */
public class MessagingConfiguration {
	private final static String BUNDLE_CONFIG = MessagingConfiguration.class.getPackage().getName() + ".config";
	
	private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_CONFIG);
	
	private static String mode = bundle.getString(ConfigConstants.MODE);
	
	public static String getConfigValue(String key) {
		return bundle.getString(mode + "." + key);
	}
}
