package gg.memz.memzserver.account.data;

public class AccountConstraints {
    public static final int MAX_SIZE_USERNAME = 16;
    public static final int MIN_SIZE_USERNAME = 5;

    public static final int MAX_SIZE_PASSWORD = 16;
    public static final int MIN_SIZE_PASSWORD = 7;

    public static enum UsernameConstraintErrorFlag{
        ERROR,
        INVALID_CHARACTERS,
        TOOLONG,
        TOOSHORT,
        ALREADYEXISTS
    }

    public static enum PasswordConstraintErrorFlag{
        ERROR,
        INVALID,
        CONTAINS_SPACES
    }

    public static enum EmailConstraintErrorFlag{
        ERROR,
        INVALID,
        ALREADYEXISTS
    }
}
