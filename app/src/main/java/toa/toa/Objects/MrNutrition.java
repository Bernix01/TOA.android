package toa.toa.Objects;

/**
 * Created by Junior on 18/07/2015.
 */
public class MrNutrition {
    private String nutritionName;
    private String nutritionImg;
    private String nutritionBack;

    public MrNutrition(String nutritionName, String nutritionImg, String nutritionBack) {
        this.nutritionName = nutritionName;
        this.nutritionImg = nutritionImg;
        this.nutritionBack = nutritionBack;
    }

    public String getNutritionName() {
        return nutritionName;
    }

    public void setNutritionName(String nutritionName) {
        this.nutritionName = nutritionName;
    }

    public String getNutritionImg() {
        return nutritionImg;
    }

    public void setNutritionImg(String nutritionImg) {
        this.nutritionImg = nutritionImg;
    }

    public String getNutritionBack() {
        return nutritionBack;
    }

    public void setNutritionBack(String nutritionBack) {
        this.nutritionBack = nutritionBack;
    }


}
