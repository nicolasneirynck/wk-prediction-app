package com.example.wk_prognose.util;

public enum Country {
    ALGERIA("Algeria", "dz"),
    ARGENTINA("Argentina", "ar"),
    AUSTRALIA("Australia", "au"),
    AUSTRIA("Austria", "at"),
    BELGIUM("Belgium", "be"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "ba"),
    BRAZIL("Brazil", "br"),
    CABO_VERDE("Cabo Verde", "cv"),
    CANADA("Canada", "ca"),
    COLOMBIA("Colombia", "co"),
    CONGO_DR("Congo DR", "cd"),
    COTE_DIVOIRE("Cote d'Ivoire", "ci"),
    CROATIA("Croatia", "hr"),
    CURACAO("Curacao", "cw"),
    CZECHIA("Czechia", "cz"),
    ECUADOR("Ecuador", "ec"),
    EGYPT("Egypt", "eg"),
    ENGLAND("England", "gb-eng"),
    FRANCE("France", "fr"),
    GERMANY("Germany", "de"),
    GHANA("Ghana", "gh"),
    HAITI("Haiti", "ht"),
    IR_IRAN("IR Iran", "ir"),
    IRAQ("Iraq", "iq"),
    JAPAN("Japan", "jp"),
    JORDAN("Jordan", "jo"),
    KOREA_REPUBLIC("Korea Republic", "kr"),
    MEXICO("Mexico", "mx"),
    MOROCCO("Morocco", "ma"),
    NETHERLANDS("Netherlands", "nl"),
    NEW_ZEALAND("New Zealand", "nz"),
    NORWAY("Norway", "no"),
    PANAMA("Panama", "pa"),
    PARAGUAY("Paraguay", "py"),
    PORTUGAL("Portugal", "pt"),
    QATAR("Qatar", "qa"),
    SAUDI_ARABIA("Saudi Arabia", "sa"),
    SCOTLAND("Scotland", "gb-sct"),
    SENEGAL("Senegal", "sn"),
    SOUTH_AFRICA("South Africa", "za"),
    SPAIN("Spain", "es"),
    SWEDEN("Sweden", "se"),
    SWITZERLAND("Switzerland", "ch"),
    TUNISIA("Tunisia", "tn"),
    TURKIYE("Turkiye", "tr"),
    USA("USA", "us"),
    URUGUAY("Uruguay", "uy"),
    UZBEKISTAN("Uzbekistan", "uz");

    private final String displayName;
    private final String isoCode;

    Country(String displayName, String isoCode) {
        this.displayName = displayName;
        this.isoCode = isoCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIsoCode() {
        return isoCode;
    }
}
