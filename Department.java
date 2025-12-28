public enum Department {
    HR("Human Resources"),
    IT("Information Technology"),
    FINANCE("Finance"),
    MARKETING("Marketing"),
    SALES("Sales"),
    OPERATIONS("Operations"),
    ADMINISTRATION("Administration");
    
    private final String displayName;
    
    Department(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

