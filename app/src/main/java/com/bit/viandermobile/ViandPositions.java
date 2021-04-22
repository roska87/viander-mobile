package com.bit.viandermobile;

import java.util.ArrayList;
import java.util.List;

public class ViandPositions {

    private Integer changePosition;
    private List<Integer> positions;

    private ViandPositions(){
        this.positions = new ArrayList<>();
    }

    public static ViandPositions initialize(){
        return new ViandPositions();
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void addPosition(int position){
        positions.add(position);
    }

    public void addChangePosition(int changePosition){
        this.changePosition = changePosition;
    }

    public boolean hasChangePosition(){
        return this.changePosition != null;
    }

    public Integer getChangePosition(){
        return this.changePosition;
    }

    public void removeChangePosition(){
        this.changePosition = null;
    }

    public void removePosition(int position){
        positions.remove((Integer) position);
    }

    public void removeAll(){
        positions.clear();
    }

    @Override
    public String toString() {
        return "ViandPositions{" +
                "positions=" + positions +
                '}';
    }
}