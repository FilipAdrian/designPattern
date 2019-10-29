package com.company;

public class Factory {
    public Player GetPlayer(PlayerType type) {
        switch (type) {
            case Citizen: {
                return new Citizen();
            }
            case Mafia: {
                return new Mafia();
            }
            case Cop: {
                return new Cop();
            }
            default: {
                throw new RuntimeException();
            }
        }
    }
}
