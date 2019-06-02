package edu.skku.map.matpwdl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//https://programmingfbf7290.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%B4%9D%EC%A0%95%EB%A6%AC%EC%8A%A4%ED%83%80%ED%8B%B0%EB%93%9C%EB%B0%94%EC%9A%B4%EB%93%9C-%EC%84%9C%EB%B9%84%EC%8A%A4
public class RuleService extends Service { //https://twinw.tistory.com/50
    public RuleService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() { //최초 생성시
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){ //백그라운드에서 실행되는 동작
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() { //서비스가 종료될때 실행되는 함수
        super.onDestroy();
    }
}
