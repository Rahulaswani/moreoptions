package co.moreoptions.shopping.Utils;

public enum ApplicationMode {
    DEV(0), MN(1), PROD(2), BETA_PROD(3),CLUSTER_PROD(4), QA(5);

    private int index;

    private ApplicationMode(int index) {
        this.setIndex(index);
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    public static ApplicationMode getApplicationMode(int index) {
        switch (index) {
            case 0 :
                return DEV;
            case 1:
                return MN;
            case 2:
                return PROD;
            case 3:
                return BETA_PROD;
            case 4:
                return CLUSTER_PROD;
            case 5:
                return QA;
            default:
                return PROD;
        }
    }
}
