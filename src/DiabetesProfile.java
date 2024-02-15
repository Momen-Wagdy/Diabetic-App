public class DiabetesProfile {
    private int age;
    private double fastingBloodGlucose;
    private double glycohemoglobin;

    public DiabetesProfile(int age, double fastingBloodGlucose, double glycohemoglobin) {
        this.age = age;
        this.fastingBloodGlucose = fastingBloodGlucose;
        this.glycohemoglobin = glycohemoglobin;
    }

    public int getAge() {
        return age;
    }

    public double getFastingBloodGlucose() {
        return fastingBloodGlucose;
    }

    public double getGlycohemoglobin() {
        return glycohemoglobin;
    }
}
