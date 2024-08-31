package Model;

public class Student {
    private Integer sno;
    private String sname;
    private String ssex;
    private String sclass;

    // Getters and Setters
    public Integer getsno() {
        return sno;
    }

    public void setsno(Integer sno) {
        this.sno = sno;
    }

    public String getsname() {
        return sname;
    }

    public void setsname(String sname) {
        this.sname = sname;
    }

    public String getssex() {
        return ssex;
    }

    public void setssex(String ssex) {
        this.ssex = ssex;
    }

    public String getsclass() {
        return sclass;
    }

    public void setsclass(String sclass) {
        this.sclass = sclass;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno=" + sno +
                ", sname='" + sname + '\'' +
                ", ssex='" + ssex + '\'' +
                ", sclass='" + sclass + '\'' +
                '}';
    }
}
