import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents the gameplay screen in the taxi simulation game.
 * The GamePlayScreen class manages all gameplay elements.
 * It handles rendering of these elements,collision detection,
 * and game logic updates based on user input.
 */
public class GamePlayScreen {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private final String OBJECTS_FILE;
    private final String WEATHER_FILE;

    private String[][] weatherInfo;
    private int weatherCount;
    private Weather[] weathers;
    private int currentWeatherIndex = 0;
    private final Image SUNNY_BACKGROUND_IMAGE;
    private final Image RAINY_BACKGROUND_IMAGE;
    private final int BACKGROUND_SPEED;
    private final int WINDOW_WIDTH;
    private double backgroundX;
    private final int WINDOW_HEIGHT;
    private double background1Y;
    private double background2Y;
    private double MAX_Y;

    private String[][] gameInfo;
    private ArrayList<Taxi> damagedTaxis;
    private Taxi taxi;
    private Driver driver;
    private final int DRIVER_EJECT_DISTANCE = 50;
    private Passenger[] passengers;
    private int passengerCount;
    private final int PASSENGER_EJECT_DISTANCE = 100;
    private Coin[] coins;
    private int coinCount;
    private Star[] stars;
    private int starCount;
    private TripDetails tripDetails;
    private boolean hasPastTrip = false;
    private int wasLastDriven;

    private ArrayList<OtherCar> otherCars;
    private final int OTHER_CAR_SPAWN_RATE = 200;
    private ArrayList<EnemyCar> enemyCars;
    private ArrayList<FireBall> fireBalls;
    private final int ENEMY_CAR_SPAWN_RATE = 400;
    private final int MAX_TIMEOUT_DURATION = 200;
    private final int INVINCIBILITY_DURATION = 1000;
    private final int SEPARATION_DURATION = 190;

    private final int FONT_SIZE;
    private final Font FONT;

    private String TOTAL_TITLE;
    private double totalPay = 0;
    private final int TOTAL_X;
    private final int TOTAL_Y;

    private String TARGET_TEXT;
    private final double TARGET;
    private final double TARGET_X;
    private final double TARGET_Y;

    private String framesText;
    private final int FRAMES_X;
    private final int FRAMES_Y;
    private final int MAX_FRAMES;
    private int framesRemaining;

    private int coinPower = 500;
    private final int COIN_POWER_X;
    private final int COIN_POWER_Y;
    private final int MAX_COIN_POWER;

    private final int PASSENGER_HEALTH_X;
    private final int PASSENGER_HEALTH_Y;
    private final int DRIVER_HEALTH_X;
    private final int DRIVER_HEALTH_Y;
    private final int TAXI_HEALTH_X;
    private final int TAXI_HEALTH_Y;
    private final int HEALTH_MULTIPLIER = 100;

    private ArrayList<Effect> effects;

    private final int ROAD_LANE_1;
    private final int ROAD_LANE_3;

    /**
     * Constructs a GamePlayScreen with the specified game and message properties.
     * Initializes background images, window dimensions, target values, health values,
     * and game object properties by reading from the provided properties files.
     *
     * @param gameProps   the properties related to the game configuration
     * @param messageProps the properties related to game messages
     */
    public GamePlayScreen(Properties gameProps, Properties messageProps) {

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        // get properties for screen background
        SUNNY_BACKGROUND_IMAGE = new Image(GAME_PROPS.getProperty("backgroundImage.sunny"));
        RAINY_BACKGROUND_IMAGE = new Image(GAME_PROPS.getProperty("backgroundImage.raining"));
        WINDOW_WIDTH = Integer.parseInt(GAME_PROPS.getProperty("window.width"));
        backgroundX = (double) WINDOW_WIDTH / 2;
        WINDOW_HEIGHT = Integer.parseInt(GAME_PROPS.getProperty("window.height"));
        background1Y = (double) WINDOW_HEIGHT / 2;
        background2Y = -1 * ((double) WINDOW_HEIGHT / 2);
        MAX_Y = WINDOW_HEIGHT * 1.5;
        BACKGROUND_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY"));
        WEATHER_FILE = GAME_PROPS.getProperty("gamePlay.weatherFile");
        weatherInfo = IOUtils.readCommaSeparatedFile(WEATHER_FILE);
        processWeatherInfo(weatherInfo);

        // read and process game information from csv file
        OBJECTS_FILE = GAME_PROPS.getProperty("gamePlay.objectsFile");
        gameInfo = IOUtils.readCommaSeparatedFile(OBJECTS_FILE);
        processGameInfo(gameInfo);

        // set other properties
        FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.info.fontSize"));
        FONT = new Font("res/FSO8BITR.TTF", FONT_SIZE);
        TARGET = Double.parseDouble(GAME_PROPS.getProperty("gamePlay.target"));
        TARGET_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.target.x"));
        TARGET_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.target.y"));

        MAX_FRAMES = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames"));
        framesRemaining = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames"));
        FRAMES_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames.x"));
        FRAMES_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.maxFrames.y"));

        TOTAL_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.earnings.x"));
        TOTAL_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.earnings.y"));

        COIN_POWER_X = Integer.parseInt(GAME_PROPS.getProperty("gameplay.coin.x"));
        COIN_POWER_Y = Integer.parseInt(GAME_PROPS.getProperty("gameplay.coin.y"));
        MAX_COIN_POWER = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.coin.maxFrames"));

        PASSENGER_HEALTH_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.passengerHealth.x"));
        PASSENGER_HEALTH_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.passengerHealth.y"));
        DRIVER_HEALTH_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.driverHealth.x"));
        DRIVER_HEALTH_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.driverHealth.y"));
        TAXI_HEALTH_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.taxiHealth.x"));
        TAXI_HEALTH_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.taxiHealth.y")) ;

        ROAD_LANE_1 = Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter1"));
        ROAD_LANE_3 = Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter3"));

        this.damagedTaxis = new ArrayList<>();
        this.otherCars = new ArrayList<>();
        this.enemyCars = new ArrayList<>();
        this.fireBalls = new ArrayList<>();
        this.effects = new ArrayList<>();
    }

    /**
     * Renders the gameplay elements on the screen, including the background, the taxi,
     * driver, passengers, other cars, and effects. Handles the logic for generating
     * random entities, managing collisions, and updating the status of passengers.
     *
     * @param input the input from the user, used to control the taxi and driver
     */
    public void render(Input input) {
        // create random entities
        generateRandomEntities();

        // draw moving background
        renderWeather();

        // render taxi
        taxi.render(input, driver.isInTaxi());
        driver.render(input, taxi.getTaxiX());
        driver.enterTaxi(taxi.getTaxiX(), taxi.getTaxiY());

        // transfer driver invincibility to taxi
        if (driver.isInTaxi() && driver.getCollisionTimeout() > taxi.getCollisionTimeout()) {
            taxi.setCollisionTimeout(driver.getCollisionTimeout());
            if (driver.isInvincible()) {
                taxi.setLastCollidedCar(null);
            }
        }

        // render other cars
        for (OtherCar otherCar : otherCars) {
            if (otherCar.getCarHealth() > 0) {
                otherCar.render(input, driver.isInTaxi());
            }
        }
        otherCars.removeIf(otherCar -> otherCar.getCarHealth() <= 0);

        // render enemy cars
        for (EnemyCar enemyCar : enemyCars) {
            if (enemyCar.getCarHealth() > 0) {
                enemyCar.render(input, driver.isInTaxi());
            }
        }
        enemyCars.removeIf(enemyCar -> enemyCar.getCarHealth() <= 0);

        for (Effect effect: effects) {
            if (effect.getFrames() > 0) {
                effect.render(input, driver.isInTaxi());
            }
        }
        effects.removeIf(effect -> effect.getFrames() <= 0);

        // render damaged taxis
        for (Taxi damagedTaxi : damagedTaxis) {
            damagedTaxi.render(input, driver.isInTaxi());
        }

        // handle collision logic
        handleCollisions();
        FONT.drawString("TAXI " + String.format("%.2f", taxi.getTaxiHealth() * HEALTH_MULTIPLIER), TAXI_HEALTH_X, TAXI_HEALTH_Y);
        FONT.drawString("DRIVER " + String.format("%.2f", driver.getHumanHealth() * HEALTH_MULTIPLIER), DRIVER_HEALTH_X, DRIVER_HEALTH_Y);

        double minPassengerHealth = 1.0;
        // render passengers
        for (int i = 0; i < passengerCount; i++) {
            passengers[i].render(input, taxi.getTaxiX(), driver.isInTaxi());

            if (passengers[i].getHumanHealth() < minPassengerHealth) {
                minPassengerHealth = passengers[i].getHumanHealth();
            }
            /*
            if taxi is empty and stops near a passenger that has not been driven passenger enters the taxi
            after entering passenger will get off if taxi is in radius of the flag or has passed the flag
            */
            if (noMovement(input) && !taxi.getTaxiOccupied() && !passengers[i].isDriven()) {

                if (passengers[i].nearTaxi(taxi.getTaxiX(), taxi.getTaxiY()) && driver.isInTaxi()) {
                    passengers[i].enterTaxi(taxi.getTaxiX(), taxi.getTaxiY());
                    taxi.setTaxiOccupied(passengers[i].isInTaxi());
                }
            } else if (passengers[i].isInTaxi() && taxi.getTaxiHealth() <= 0) {
                passengers[i].setXPos(passengers[i].getXPos() - PASSENGER_EJECT_DISTANCE);
                passengers[i].setInTaxi(false);
                taxi.setTaxiOccupied(false);
            } else if ((noMovement(input) || !passengers[i].isInTaxi()) && passengers[i].isTripOngoing() && driver.isInTaxi()) {
                passengers[i].checkTripPosition(taxi.getTaxiX(), taxi.getTaxiY());
                taxi.setTaxiOccupied(passengers[i].isInTaxi());
            }

            // set values for current trip
            if (passengers[i].isInTaxi() ||
                    (!passengers[i].isInTaxi() && passengers[i].isTripOngoing() && !passengers[i].isDriven())) {
                tripDetails = new TripDetails(MESSAGE_PROPS, GAME_PROPS, passengers[i], taxi.getTaxiX(),
                        taxi.getTaxiY(), passengers[i].getFlagX(), passengers[i].getFlagY());
            }
            /*
            print current trip or last trip details depending on occupancy status of taxi and
            render current passenger health or minimum passenger health
            */
            if (passengers[i].isInTaxi() ||
                    (!passengers[i].isInTaxi() && passengers[i].isTripOngoing() && !passengers[i].isDriven())) {
                tripDetails.renderCurrent();
                hasPastTrip = true;
                wasLastDriven = i;

                FONT.drawString("PASSENGER " + String.format("%.2f", passengers[i].getHumanHealth() * HEALTH_MULTIPLIER),
                        PASSENGER_HEALTH_X, PASSENGER_HEALTH_Y);
            } else if (!passengers[i].isInTaxi() && hasPastTrip && (i == wasLastDriven) && !taxi.getTaxiOccupied()) {
                tripDetails.renderLast();

                // all trip earnings to total pay
                if (!tripDetails.isEarningsCalculated()) {
                    totalPay += tripDetails.getFinalExp();
                    tripDetails.setEarningsCalculated(true);
                }

                FONT.drawString("PASSENGER " + String.format("%.2f", minPassengerHealth * HEALTH_MULTIPLIER),
                        PASSENGER_HEALTH_X, PASSENGER_HEALTH_Y);
            }

            // update passenger priority when coin is collected or taxi is in coin power form
            if ((passengers[i].isInTaxi()
                    || !passengers[i].isInTaxi() && passengers[i].isTripOngoing() && !passengers[i].isDriven())
                    && coinPower < MAX_COIN_POWER && !passengers[i].isPoweredUp()) {
                passengers[i].setPoweredUp(true);
            }
        }

        // render total stats
        totalDetails();
        FONT.drawString(TOTAL_TITLE + String.format("%.2f", totalPay), TOTAL_X, TOTAL_Y);
        FONT.drawString(TARGET_TEXT + String.format("%.2f", TARGET), TARGET_X, TARGET_Y);
        FONT.drawString( framesText + framesRemaining, FRAMES_X, FRAMES_Y);

        //render power ups
        renderPowerUps(input);

    }

    // handle all random logic
    private void generateRandomEntities() {
        if (MiscUtils.canSpawn(OTHER_CAR_SPAWN_RATE)) {
            // Create a new OtherCar instance and add it to the list
            OtherCar newOtherCar = new OtherCar(GAME_PROPS);
            otherCars.add(newOtherCar);
        }
        if (MiscUtils.canSpawn(ENEMY_CAR_SPAWN_RATE)) {
            // Create a new EnemyCar instance and add it to the list
            EnemyCar newEnemyCar = new EnemyCar(GAME_PROPS);
            enemyCars.add(newEnemyCar);
        }
        if (taxi.getTaxiHealth() <= 0) {
            taxi.setDamaged(true);
            damagedTaxis.add(taxi);
            if (driver.isInTaxi()) {
                driver.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                driver.setXPos(driver.getXPos() - DRIVER_EJECT_DISTANCE);
                driver.setInTaxi(false);
            }

            double XPos = MiscUtils.selectAValue(ROAD_LANE_1, ROAD_LANE_3);
            double YPos = MiscUtils.getRandomInt(OTHER_CAR_SPAWN_RATE, ENEMY_CAR_SPAWN_RATE);

            taxi = new Taxi(GAME_PROPS, XPos, YPos);
        }
    }

    // handle background weather rendering
    private void renderWeather() {
        if (MAX_FRAMES - framesRemaining > weathers[currentWeatherIndex].getEnd()) {
            currentWeatherIndex++;
        }

        String currentWeather = weathers[currentWeatherIndex].getWeather();

        if(currentWeather.equals("SUNNY")) {
            SUNNY_BACKGROUND_IMAGE.draw(backgroundX, background1Y);
            SUNNY_BACKGROUND_IMAGE.draw(backgroundX, background2Y);
        } else if (currentWeather.equals("RAINING")) {
            RAINY_BACKGROUND_IMAGE.draw(backgroundX, background1Y);
            RAINY_BACKGROUND_IMAGE.draw(backgroundX, background2Y);

            for (Passenger passenger: passengers) {
                if (passenger.getHAS_UMBRELLA() == 0) {
                    passenger.setPriority(1);
                }
            }
        }
    }

    // handle all power up logic
    private void renderPowerUps(Input input) {
        // render coins and check for collisions
        for (int j = 0; j < coinCount; j++) {
            coins[j].render(input, driver.isInTaxi());

            if (!coins[j].isHasCollided()) {
                coins[j].checkCollision(taxi.getTaxiX(), taxi.getTaxiY());
                if (!driver.isInTaxi()) {
                    coins[j].checkCollision(driver.getXPos(), driver.getYPos());
                }
                if (coins[j].isHasCollided()) {
                    coinPower = 0;
                }
            }
        }
        if (coinPower < MAX_COIN_POWER) {
            coinPower += 1;
        }
        if (coinPower < MAX_COIN_POWER && driver.isInTaxi()) {
            FONT.drawString(String.valueOf(coinPower), COIN_POWER_X, COIN_POWER_Y);
        }

        // render Invincible power and check for collisions
        for (int k = 0; k < starCount; k++) {
            stars[k].render(input, driver.isInTaxi());

            if (!stars[k].isHasCollided()) {
                if (driver.isInTaxi()) {
                    stars[k].checkCollision(taxi.getTaxiX(), taxi.getTaxiY());
                    if (stars[k].isHasCollided()) {
                        taxi.setCollisionTimeout(INVINCIBILITY_DURATION);
                        taxi.setLastCollidedCar(null);
                        taxi.setHitFireBall(false);
                    }
                } else {
                    stars[k].checkCollision(driver.getXPos(), driver.getYPos());
                    if (stars[k].isHasCollided()) {
                        driver.setCollisionTimeout(INVINCIBILITY_DURATION);
                        driver.setInvincible(true);
                    }
                }
            }
        }
    }

    // handles collision logic
    private void handleCollisions() {
        handleOtherCarCollisions();

        handleEnemyCarCollisions();

        // handle taxi separation
        if (taxi.getCollisionTimeout() > 0) {
            if (taxi.getCollisionTimeout() > SEPARATION_DURATION && (taxi.getLastCollidedCar() != null)) {
                if (taxi.getLastCollidedCar().getYPos() > taxi.getTaxiY()) {
                    taxi.setTaxiY(taxi.getTaxiY() - 1);
                } else {
                    taxi.setTaxiY(taxi.getTaxiY() + 1);
                }
            } else if (taxi.getCollisionTimeout() > SEPARATION_DURATION && taxi.isHitFireBall()) {
                taxi.setTaxiY(taxi.getTaxiY() - 1);
            }
            taxi.setCollisionTimeout(taxi.getCollisionTimeout() - 1);
        }

        if (driver.getCollisionTimeout() > 0) {
            driver.setCollisionTimeout(driver.getCollisionTimeout() - 1);
        }

        for (Passenger passenger: passengers) {
            if (passenger.getCollisionTimeout() > 0) {
                passenger.setCollisionTimeout(passenger.getCollisionTimeout() - 1);
            }
        }
    }

    // handles collisions for other cars
    private void handleOtherCarCollisions() {
        for (int i = 0; i < otherCars.size(); i++) {
            OtherCar otherCar = otherCars.get(i);

            // Check collision with the Taxi
            if (taxi.hasCollided(otherCar.getXPos(), otherCar.getYPos(), otherCar.getCAR_RADIUS()) && taxi.getCollisionTimeout() == 0) {
                taxi.setTaxiHealth(otherCar.getCAR_DAMAGE());
                taxi.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                taxi.setLastCollidedCar(otherCar);

                addEffect(taxi.getTaxiHealth(), taxi.getTaxiX(), taxi.getTaxiY());
            }

            if (otherCar.hasCollided(taxi.getTaxiX(), taxi.getTaxiY(), taxi.getTAXI_RADIUS())
                    && otherCar.getCollisionTimeout() == 0) {
                otherCar.setCarHealth(otherCar.getCarHealth() - taxi.getTAXI_DAMAGE());
                otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);

                addEffect(otherCar.getCarHealth(), otherCar.getXPos(), otherCar.getYPos());
            }

            // Check collisions with other OtherCar instances
            for (int j = i + 1; j < otherCars.size(); j++) {
                OtherCar otherCar2 = otherCars.get(j);

                if (otherCar.hasCollided(otherCar2.getXPos(), otherCar2.getYPos(), otherCar2.getCAR_RADIUS())
                        && otherCar.getCollisionTimeout() == 0) {
                    otherCar.setCarHealth(otherCar.getCarHealth() - otherCar2.getCAR_DAMAGE());
                    otherCar2.setCarHealth(otherCar2.getCarHealth() - otherCar.getCAR_DAMAGE());


                    otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    otherCar2.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    if (otherCar2.getYPos() > otherCar.getYPos()) {
                        otherCar.setMoveForward(true);
                        otherCar2.setMoveForward(false);
                    } else {
                        otherCar.setMoveForward(false);
                        otherCar2.setMoveForward(true);
                    }

                    addEffect(otherCar.getCarHealth(), otherCar.getXPos(), otherCar.getYPos());
                    addEffect(otherCar2.getCarHealth(), otherCar2.getXPos(), otherCar2.getYPos());
                }
            }

            if (otherCar.getCollisionTimeout() > 0) {
                otherCar.setCollisionTimeout(otherCar.getCollisionTimeout() - 1);
            }

            if (driver.hasCollided(otherCar.getXPos(), otherCar.getYPos(), otherCar.getCAR_RADIUS()) && driver.getCollisionTimeout() == 0
                    && !driver.isInTaxi()) {
                driver.setHumanHealth(driver.getHumanHealth() - otherCar.getCAR_DAMAGE());
                driver.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                if (otherCar.getCollisionTimeout() == 0) {
                    otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                }
                if (otherCar.getYPos() > driver.getYPos()) {
                    driver.setMoveForward(true);
                    otherCar.setMoveForward(false);
                } else {
                    driver.setMoveForward(false);
                    otherCar.setMoveForward(true);
                }
            }

            for (Passenger passenger: passengers) {
                if (passenger.hasCollided(otherCar.getXPos(), otherCar.getYPos(), otherCar.getCAR_RADIUS()) && passenger.getCollisionTimeout() == 0
                        && passenger.isTripOngoing() && !passenger.isInTaxi()) {
                    passenger.setHumanHealth(passenger.getHumanHealth() - otherCar.getCAR_DAMAGE());
                    passenger.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    if (otherCar.getCollisionTimeout() == 0) {
                        otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    }
                    if (otherCar.getYPos() > passenger.getYPos()) {
                        passenger.setMoveForward(true);
                        otherCar.setMoveForward(false);
                    } else {
                        passenger.setMoveForward(false);
                        otherCar.setMoveForward(true);
                    }
                }
            }

            for (EnemyCar enemyCar: enemyCars) {
                if (enemyCar.hasCollided(otherCar.getXPos(), otherCar.getYPos(), otherCar.getCAR_RADIUS())
                        && enemyCar.getCollisionTimeout() == 0) {
                    enemyCar.setCarHealth(enemyCar.getCarHealth() - otherCar.getCAR_DAMAGE());
                    enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);

                    addEffect(enemyCar.getCarHealth(), enemyCar.getXPos(), enemyCar.getYPos());

                    if (otherCar.getCollisionTimeout() == 0) {
                        otherCar.setCarHealth(otherCar.getCarHealth() - enemyCar.getCAR_DAMAGE());
                        otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);

                        addEffect(otherCar.getCarHealth(), otherCar.getXPos(), otherCar.getYPos());
                    }
                    if (otherCar.getYPos() > enemyCar.getYPos()) {
                        enemyCar.setMoveForward(true);
                        otherCar.setMoveForward(false);
                    } else {
                        enemyCar.setMoveForward(false);
                        otherCar.setMoveForward(true);
                    }
                }
            }
        }
    }

    // handles collisions for enemy cars
    private void handleEnemyCarCollisions() {
        for (int i = 0; i < enemyCars.size(); i++) {
            EnemyCar enemyCar = enemyCars.get(i);

            // handle fireball rendering with current instance of enemy car
            for (FireBall fireBall: enemyCar.getFireBalls()) {
                if (!fireBalls.contains(fireBall)) {
                    fireBalls.add(fireBall);
                }
            }
            fireBalls.removeIf(FireBall::HasCollided);

            handleFireballCollisions(enemyCar);

            if (taxi.hasCollided(enemyCar.getXPos(), enemyCar.getYPos(), enemyCar.getCAR_RADIUS()) && taxi.getCollisionTimeout() == 0) {
                taxi.setTaxiHealth(enemyCar.getCAR_DAMAGE());
                taxi.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                taxi.setLastCollidedCar(enemyCar);

                addEffect(taxi.getTaxiHealth(), taxi.getTaxiX(), taxi.getTaxiY());
            }

            if (enemyCar.hasCollided(taxi.getTaxiX(), taxi.getTaxiY(), taxi.getTAXI_RADIUS())
                    && enemyCar.getCollisionTimeout() == 0) {
                enemyCar.setCarHealth(enemyCar.getCarHealth() - taxi.getTAXI_DAMAGE());
                enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);

                addEffect(enemyCar.getCarHealth(), enemyCar.getXPos(), enemyCar.getYPos());
            }


            // Check collisions with other OtherCar instances
            for (int j = i + 1; j < enemyCars.size(); j++) {
                EnemyCar enemyCar2 = enemyCars.get(j);

                if (enemyCar.hasCollided(enemyCar2.getXPos(), enemyCar2.getYPos(), enemyCar2.getCAR_RADIUS())
                        && enemyCar.getCollisionTimeout() == 0) {
                    enemyCar.setCarHealth(enemyCar.getCarHealth() - enemyCar2.getCAR_DAMAGE());
                    enemyCar2.setCarHealth(enemyCar2.getCarHealth() - enemyCar.getCAR_DAMAGE());

                    enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    enemyCar2.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    if (enemyCar2.getYPos() > enemyCar.getYPos()) {
                        enemyCar.setMoveForward(true);
                        enemyCar2.setMoveForward(false);
                    } else {
                        enemyCar.setMoveForward(false);
                        enemyCar2.setMoveForward(true);
                    }

                    addEffect(enemyCar.getCarHealth(), enemyCar.getXPos(), enemyCar.getYPos());
                    addEffect(enemyCar2.getCarHealth(), enemyCar2.getXPos(), enemyCar2.getYPos());
                }
            }
            if (enemyCar.getCollisionTimeout() > 0) {
                enemyCar.setCollisionTimeout(enemyCar.getCollisionTimeout() - 1);
            }

            if (driver.hasCollided(enemyCar.getXPos(), enemyCar.getYPos(), enemyCar.getCAR_RADIUS()) && driver.getCollisionTimeout() == 0
                    && !driver.isInTaxi()) {
                driver.setHumanHealth(driver.getHumanHealth() - enemyCar.getCAR_DAMAGE());
                driver.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                if (enemyCar.getCollisionTimeout() == 0) {
                    enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                }
                if (enemyCar.getYPos() > driver.getYPos()) {
                    driver.setMoveForward(true);
                    enemyCar.setMoveForward(false);
                } else {
                    driver.setMoveForward(false);
                    enemyCar.setMoveForward(true);
                }
            }

            for (Passenger passenger: passengers) {
                if (passenger.hasCollided(enemyCar.getXPos(), enemyCar.getYPos(), enemyCar.getCAR_RADIUS()) && passenger.getCollisionTimeout() == 0
                        && passenger.isTripOngoing() && !passenger.isInTaxi()) {
                    passenger.setHumanHealth(passenger.getHumanHealth() - enemyCar.getCAR_DAMAGE());
                    passenger.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    if (enemyCar.getCollisionTimeout() == 0) {
                        enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    }
                    if (enemyCar.getYPos() > passenger.getYPos()) {
                        passenger.setMoveForward(true);
                        enemyCar.setMoveForward(false);
                    } else {
                        passenger.setMoveForward(false);
                        enemyCar.setMoveForward(true);
                    }
                }
            }
        }
    }

    // handles all fireball collisions
    private void handleFireballCollisions(EnemyCar enemyCar) {
        for (FireBall fireBall: fireBalls) {
            if (taxi.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS()) && taxi.getCollisionTimeout() == 0) {
                taxi.setTaxiHealth(fireBall.getFIRE_BALL_DAMAGE());
                taxi.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                fireBall.setHasCollided(true);

                addEffect(taxi.getTaxiHealth(), taxi.getTaxiX(), taxi.getTaxiY());
                taxi.setHitFireBall(true);
            } else if (taxi.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())) {
                fireBall.setHasCollided(true);
            }

            if (enemyCar.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                    && enemyCar.getCollisionTimeout() == 0 && !enemyCar.getFireBalls().contains(fireBall)) {
                enemyCar.setCarHealth(enemyCar.getCarHealth() - fireBall.getFIRE_BALL_DAMAGE());
                enemyCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                fireBall.setHasCollided(true);

                addEffect(enemyCar.getCarHealth(), enemyCar.getXPos(), enemyCar.getYPos());
            } else if (enemyCar.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                    && !enemyCar.getFireBalls().contains(fireBall)) {
                fireBall.setHasCollided(true);
            }

            if (driver.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                    && driver.getCollisionTimeout() == 0 && !driver.isInTaxi()) {
                driver.setHumanHealth(driver.getHumanHealth() - fireBall.getFIRE_BALL_DAMAGE());
                driver.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                driver.setMoveForward(true);
                fireBall.setHasCollided(true);
            } else if (driver.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())) {
                fireBall.setHasCollided(true);
            }

            for (Passenger passenger: passengers) {
                if (passenger.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                        && passenger.getCollisionTimeout() == 0 && passenger.isTripOngoing() && !passenger.isInTaxi()) {
                    passenger.setHumanHealth(passenger.getHumanHealth() - fireBall.getFIRE_BALL_DAMAGE());
                    passenger.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    passenger.setMoveForward(true);
                    fireBall.setHasCollided(true);
                } else if (passenger.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                        && passenger.isTripOngoing() && !passenger.isInTaxi()) {
                    fireBall.setHasCollided(true);
                }
            }

            for (OtherCar otherCar: otherCars) {
                if (otherCar.hasCollided(fireBall.getXPos(), fireBall.getYPos(), fireBall.getFIRE_BALL_RADIUS())
                        && otherCar.getCollisionTimeout() == 0) {
                    otherCar.setCarHealth(otherCar.getCarHealth() - fireBall.getFIRE_BALL_DAMAGE());
                    otherCar.setCollisionTimeout(MAX_TIMEOUT_DURATION);
                    otherCar.setMoveForward(true);
                    fireBall.setHasCollided(true);

                    addEffect(otherCar.getCarHealth(), otherCar.getXPos(), otherCar.getYPos());
                } else if (otherCar.hasCollided(fireBall.getXPos(), fireBall.getYPos(),
                        fireBall.getFIRE_BALL_RADIUS())) {
                    fireBall.setHasCollided(true);
                }
            }
        }
    }

    // add to effects array list
    private void addEffect(double health, double XPos, double YPos) {
        if (health > 0) {
            effects.add(new Smoke(GAME_PROPS, XPos, YPos));
        } else {
            effects.add(new Fire(GAME_PROPS, XPos, YPos));
        }    }

    /**
     * Handles user input for controlling the game.
     * Moves the background up or down based on the UP and DOWN key presses.
     * The speed varies depending on whether the driver is in the taxi.
     *
     * @param input the user input used to determine movement direction
     */
    public void handleInput(Input input) {
        if (input.isDown(Keys.UP)) {
            if (driver.isInTaxi()) {
                background1Y += BACKGROUND_SPEED;
                background2Y += BACKGROUND_SPEED;
            } else {
                background1Y += 1;
                background2Y += 1;
            }

            if (background1Y >= MAX_Y) {
                background1Y = background2Y - WINDOW_HEIGHT;
            }
            if (background2Y >= MAX_Y) {
                background2Y = background1Y - WINDOW_HEIGHT;
            }
        } else if (input.isDown(Keys.DOWN)) {
            if (!driver.isInTaxi()) {
                background1Y -= 1;
                background2Y -= 1;
            }
        }
    }

    // check for horizontal or vertical movement
    private boolean noMovement(Input input) {
        return !input.isDown(Keys.UP) && !input.isDown(Keys.DOWN) &&
                !input.isDown(Keys.LEFT) && !input.isDown(Keys.RIGHT);
    }

    // process csv files containing all entity information
    private void processGameInfo(String[][] gameInfo) {
        // initialize passenger and coin arrays
        passengerCount = 0;
        coinCount = 0;
        starCount = 0;
        for (String[] row : gameInfo) {
            String entity = row[0];
            if (entity.equals("PASSENGER")) {
                passengerCount += 1;
            } else if (entity.equals("COIN")) {
                coinCount += 1;
            } else if (entity.equals("INVINCIBLE_POWER")) {
                starCount += 1;
            }
        }
        passengers = new Passenger[passengerCount];
        coins = new Coin[coinCount];
        stars = new Star[starCount];

        // scan csv file processing entity information
        int passengerIndex = 0;
        int coinIndex = 0;
        int starIndex = 0;
        for (String[] row : gameInfo) {
            String entity = row[0];
            if (entity.equals("TAXI")) {
                double x = Double.parseDouble(row[1]);
                double y = Double.parseDouble(row[2]);
                taxi = new Taxi(GAME_PROPS, x, y);
            } else if (entity.equals("DRIVER")) {
                double x = Double.parseDouble(row[1]);
                double y = Double.parseDouble(row[2]);
                driver = new Driver(GAME_PROPS, x, y);
            } else if (entity.equals("PASSENGER")) {
                double x = Double.parseDouble(row[1]);
                double y = Double.parseDouble(row[2]);
                String priority = row[3];
                int endX = Integer.parseInt(row[4]);
                int distanceY = Integer.parseInt(row[5]);
                int hasUmbrella = Integer.parseInt(row[6]);
                passengers[passengerIndex++] = new Passenger(GAME_PROPS, x, y, priority, endX, distanceY, hasUmbrella);
            } else if (entity.equals("COIN")) {
                double x = Double.parseDouble(row[1]);
                double y = Double.parseDouble(row[2]);
                coins[coinIndex++] = new Coin(GAME_PROPS, x, y);
            } else if (entity.equals("INVINCIBLE_POWER")) {
                double x = Double.parseDouble(row[1]);
                double y = Double.parseDouble(row[2]);
                stars[starIndex++] = new Star(GAME_PROPS, x, y);
            }
        }
    }

    // read weather csv
    private void processWeatherInfo(String[][] weatherInfo) {
        weatherCount = 0;
        for (String[] row : weatherInfo) {
            weatherCount++;
        }

        weathers = new Weather[weatherCount];
        int weatherIndex = 0;
        for (String[] row : weatherInfo) {
            String weather = row[0];
            int start = Integer.parseInt(row[1]);
            int end = Integer.parseInt(row[2]);
            weathers[weatherIndex++] = new Weather(weather, start, end);
        }
    }

    // set details for total trip statistics
    private void totalDetails() {
        TOTAL_TITLE = MESSAGE_PROPS.getProperty("gamePlay.earnings");
        TARGET_TEXT = MESSAGE_PROPS.getProperty("gamePlay.target");
        framesText = MESSAGE_PROPS.getProperty("gamePlay.remFrames");
        if (framesRemaining > 0) {
            framesRemaining -= 1;
        }
    }

    /**
     * Checks if the game is won by comparing total payment to the target amount.
     * @return true if the total payment meets or exceeds the target, false otherwise.
     */
    public boolean isGameWon() {
        if (totalPay >= TARGET) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the game is lost based on several conditions:
     * @return true if the game is lost, false otherwise.
     */
    public boolean isGameLost() {
        if (framesRemaining == 0) {
            return true;
        } else if (taxi.getTaxiY() > WINDOW_HEIGHT) {
            return true;
        } else if (driver.getHumanHealth() <= 0) {
            return true;
        }
        for (Passenger passenger: passengers) {
            if (passenger.getHumanHealth() <= 0) {
                return true;
            }
        }

        return false;
    }

    //getters
    public double getTotalPay() {
        return totalPay;
    }

}