// Интерфейс ингредиента
interface Ingredient {
    String getName();
}

// Классы ингредиентов
class CoffeeBeans implements Ingredient {
    public String getName() {
        return "Coffee beans";
    }
}

class TeaLeaves implements Ingredient {
    public String getName() {
        return "Tea leaves";
    }
}

// Интерфейс напитка
interface Beverage {
    void prepare();
}

// Конкретные напитки
class Coffee implements Beverage {
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    public Coffee(Ingredient ingredient1, Ingredient ingredient2) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    public void prepare() {
        System.out.println("Making coffee from " + ingredient1.getName() + " and " + ingredient2.getName());
    }
}

class Tea implements Beverage {
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    public Tea(Ingredient ingredient1, Ingredient ingredient2) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    public void prepare() {
        System.out.println("Brewing tea from " + ingredient1.getName() + " and " + ingredient2.getName());
    }
}

// Интерфейс фабрики напитков
interface BeverageFactory {
    Beverage createBeverage();
}

// Конкретные фабрики напитков
class CoffeeFactory implements BeverageFactory {
    public Beverage createBeverage() {
        return new Coffee(new CoffeeBeans(), new Ingredient() {
            public String getName() {
                return "Water";
            }
        });
    }
}

class TeaFactory implements BeverageFactory {
    public Beverage createBeverage() {
        return new Tea(new TeaLeaves(), new Ingredient() {
            public String getName() {
                return "Water";
            }
        });
    }
}

public class Main {
    public static void main(String[] args) {
        // Создание фабрик напитков
        BeverageFactory coffeeFactory = new CoffeeFactory();
        BeverageFactory teaFactory = new TeaFactory();

        // Создание напитков с использованием фабрик
        Beverage coffee = coffeeFactory.createBeverage();
        Beverage tea = teaFactory.createBeverage();

        // Приготовление напитков
        coffee.prepare();
        tea.prepare();
    }
}
