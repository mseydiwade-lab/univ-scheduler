package utils;

import java.security.SecureRandom;

public class PasswordGenerator {
    
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL;
    private static final int PASSWORD_LENGTH = 12;
    
    private static final SecureRandom random = new SecureRandom();
    
    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        
        // Assurer au moins un caractère de chaque type
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        
        // Remplir le reste avec des caractères aléatoires
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        
        // Mélanger les caractères
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}
