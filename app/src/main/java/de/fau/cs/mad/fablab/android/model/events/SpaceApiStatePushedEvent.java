package de.fau.cs.mad.fablab.android.model.events;

import de.fau.cs.mad.fablab.rest.core.DoorState;

public class SpaceApiStatePushedEvent {
    private final DoorState mDoorState;

    public SpaceApiStatePushedEvent(DoorState doorState){
        mDoorState = doorState;
    }

    public DoorState getDoorState() {
        return mDoorState;
    }
}
