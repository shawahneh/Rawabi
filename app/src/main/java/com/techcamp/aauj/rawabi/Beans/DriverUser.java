package com.techcamp.aauj.rawabi.Beans;

/**
 * Created by alaam on 11/15/2017.
 */

public class DriverUser extends RiderUser {
    private Car car;

    public DriverUser(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    class Car{
        private String name,model,color;

        public Car(String name, String model, String color) {
            this.name = name;
            this.model = model;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

}
