package in.jploft.esevak.pojo;

public class CategoryData {
    private int position;
    private String name;
    private int categoryImg;

    public CategoryData(int position, String name, int categoryImg) {
        this.position = position;
        this.name = name;
        this.categoryImg = categoryImg;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(int categoryImg) {
        this.categoryImg = categoryImg;
    }
}
