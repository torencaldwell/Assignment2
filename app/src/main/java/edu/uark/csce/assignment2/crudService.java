package edu.uark.csce.assignment2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class crudService extends Service {
    public crudService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
