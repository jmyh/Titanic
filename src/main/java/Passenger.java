public class Passenger {
    public final static String MALE="male";
    public final static String FEMALE="female";

    public final static int INT_NULL=-1;

    public final static char SOUTHAMPTON='S';
    public final static char CHERBOURG='C';
    public final static char QUEENSTOWN='Q';

    public final static String UNKNOWN="NA";

    public final static int MASK_ID=32768;
    public final static int MASK_SURVIVED=16384;
    public final static int MASK_PCLASS=8192;
    public final static int MASK_NAME=4096;
    public final static int MASK_SEX=2048;
    public final static int MASK_AGE=1024;
    public final static int MASK_SIBSP=512;
    public final static int MASK_PARCH=256;
    public final static int MASK_TICKET=128;
    public final static int MASK_FARE=64;
    public final static int MASK_CABIN=32;
    public final static int MASK_EMBARKED=16;
    public final static int MASK_FAMILY=8;
    public final static int MASK_ALONE=4;
    public final static int MASK_FARECATEGORY=2;
    public final static int MASK_SALUTATION=1;

    public final static int MASK_DEFAULT=MASK_SURVIVED+MASK_PCLASS+MASK_SEX+MASK_AGE+MASK_EMBARKED+MASK_FAMILY+MASK_ALONE+MASK_FARECATEGORY;

    private int id;
    private Boolean survived;
    private int pClass;
    private String name;
    private String sex;
    private float age;
    private int sibSp;
    private int parch;
    private String ticket;
    private double fare;
    private String cabin;
    private Character embarked;
    //upgraded field
    private int family;
    private boolean alone;
    private FareCategory fareCategory;
    private String salutation;




    public Passenger(String id, String survived, String pClass, String name, String sex, String age, String sibSp, String parch, String ticket, String fare, String cabin, String embarked) {
        this.id = id.equals("")?Passenger.INT_NULL:Integer.parseInt(id);
        this.survived = survived.equals("")?null:(Integer.parseInt(survived))==1?true:false;
        this.pClass = pClass.equals("")?0:Integer.parseInt(pClass);
        this.name = name.equals("")?null:name;
        this.sex = sex.equals("")?null:sex;
        this.age = age.equals("")?INT_NULL:Float.parseFloat(age);
        this.sibSp = sibSp.equals("")?INT_NULL:Integer.parseInt(sibSp);
        this.parch = parch.equals("")?-INT_NULL:Integer.parseInt(parch);
        this.ticket = ticket.equals("")?null:ticket;
        this.fare = fare.equals("")?INT_NULL:Double.parseDouble(fare);
        this.cabin = cabin.equals("")?null:cabin;
        this.embarked = embarked.equals("")?null:embarked.charAt(0);
        //upgraded field
        this.family=this.sibSp+this.parch;
        this.alone=(this.family==0)?true:false;

        if(survived!=null) {
            if(this.fare>0 && this.fare<=7.90) fareCategory=FareCategory.LOW;
            else if (this.fare>=7.90 && this.fare<14.45) fareCategory=FareCategory.MIDDLE;
            else if (this.fare>=14.45 && this.fare<31.28) fareCategory=FareCategory.HIGH_MIDDLE;
            else fareCategory=FareCategory.HIGH;
        }

        String[] tempStr=this.name.split(",");
        if(tempStr.length>1)
            this.salutation=tempStr[1].split("\\.")[0].trim();  //selection prefix such as Mr, Miss & etc.
        else this.salutation=Passenger.UNKNOWN;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean isSurvived() {
        return survived;
    }

    public void setSurvived(boolean survived) {
        this.survived = survived;
    }

    public int getpClass() {
        return pClass;
    }

    public void setpClass(int pClass) {
        this.pClass = pClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public int getSibSp() {
        return sibSp;
    }

    public void setSibSp(int sibSp) {
        this.sibSp = sibSp;
    }

    public int getParch() {
        return parch;
    }

    public void setParch(int parch) {
        this.parch = parch;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public Character getEmbarked() {
        return embarked;
    }

    public void setEmbarked(char embarked) {
        this.embarked = embarked;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public boolean isAlone() {
        return alone;
    }

    public void setAlone(boolean alone) {
        this.alone = alone;
    }

    public FareCategory getFareCategory() {
        return fareCategory;
    }

    public void setFareCategory(FareCategory fareCategory) {
        this.fareCategory = fareCategory;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String toCSVString(int mask) {
        String[] elements=new String[16];
        StringBuilder result=new StringBuilder();

        if((mask>>15)%2==1) elements[0]=String.valueOf(id);
        if((mask>>14)%2==1) elements[1]=survived.toString();
        if((mask>>13)%2==1) elements[2]=String.valueOf(pClass);
        if((mask>>12)%2==1) elements[3]=name;
        if((mask>>11)%2==1) elements[4]=sex;
        if((mask>>10)%2==1) elements[5]=String.valueOf(age);
        if((mask>>9)%2==1) elements[6]=String.valueOf(sibSp);
        if((mask>>8)%2==1) elements[7]=String.valueOf(parch);
        if((mask>>7)%2==1) elements[8]=ticket;
        if((mask>>6)%2==1) elements[9]=String.valueOf(fare);
        if((mask>>5)%2==1) elements[10]=cabin;
        if((mask>>4)%2==1) elements[11]=String.valueOf(embarked);
        if((mask>>3)%2==1) elements[12]=String.valueOf(family);
        if((mask>>2)%2==1) elements[13]=String.valueOf(alone);
        if((mask>>1)%2==1) elements[14]=String.valueOf(fareCategory);
        if((mask>>0)%2==1) elements[15]=String.valueOf(salutation);

        for(int i=0;i<elements.length;i++) {
            if(elements[i]!=null) {
                result.append(elements[i]);
                if(i!=elements.length-1) result.append(",");
            }
        }
        result.deleteCharAt(result.length()-1);

        return result.toString();
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", survived=" + survived +
                ", pClass=" + pClass +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", sibSp=" + sibSp +
                ", parch=" + parch +
                ", ticket='" + ticket + '\'' +
                ", fare=" + fare +
                ", cabin='" + cabin + '\'' +
                ", embarked=" + embarked +
                ", family=" + family +
                ", alone=" + alone +
                ", fareCategory=" + fareCategory +
                ", salutation='" + salutation + '\'' +
                '}';
    }
}
