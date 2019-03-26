package enums;

public enum JobStatus {
    IN_BIDDING, IN_PROGRESS, COMPLETED, EXPIRED;

    public String toString(){
        switch(this){
            case IN_BIDDING :
                return "in bidding";
            case IN_PROGRESS :
                return "in progress";
            case COMPLETED :
                return "completed";
            case EXPIRED:
                return "expired";
        }
        return null;
    }
}
