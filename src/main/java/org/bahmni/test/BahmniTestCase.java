package org.bahmni.test;

import org.bahmni.test.page.login.LoginPage;
import org.bahmni.test.page.PageFactory;

public class BahmniTestCase {

	public static LoginPage start(){
		return PageFactory.getLoginPage();
	}

}
