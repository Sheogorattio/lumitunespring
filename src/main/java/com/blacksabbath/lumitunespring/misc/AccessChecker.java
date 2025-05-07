package com.blacksabbath.lumitunespring.misc;

import jakarta.servlet.http.HttpServletRequest;

public class AccessChecker {

	    public static boolean Check(HttpServletRequest request, String nickname) {
        Object roleAttr = request.getAttribute("role");
        Object nameAttr = request.getAttribute("username");

        if (roleAttr == null || nameAttr == null) {
            return false;
        }

        String role = roleAttr.toString();
        String userNickname = nameAttr.toString();
        
        System.out.println(AccessChecker.class.getName() + ":Check: role attribute is: " + role);

        if (role.equals(Roles.ADMIN.toString())) {
            return true;
        }

        return userNickname.equals(nickname);
    }
	
}
