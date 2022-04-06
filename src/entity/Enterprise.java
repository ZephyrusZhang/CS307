package entity;

public class Enterprise {
    public String enterprise_name;
    public String industry;
    public int location_id;
    public String supply_center;

    public Enterprise(String enterprise_name, String industry, int location_id, String supply_center) {
        this.enterprise_name = enterprise_name;
        this.industry = industry;
        this.location_id = location_id;
        this.supply_center = supply_center;
    }
}
