package cn.springcloud.gray.client.plugin.event.longpolling;

import cn.springcloud.gray.client.plugin.event.longpolling.configuration.properties.LongPollingProperties;
import cn.springcloud.gray.local.InstanceLocalInfoInitiralizer;
import cn.springlcoud.gray.event.client.BasicGrayEventReceiver;
import cn.springlcoud.gray.event.client.GrayEventPublisher;

/**
 * @author saleson
 * @date 2020-02-04 00:40
 */
public class GrayEventLongPollingReceiver extends BasicGrayEventReceiver {

    private volatile long currentSortMark;
    private GrayEventPublisher grayEventPublisher;
    private GrayEventRemoteClient grayEventRemoteClient;
    private LongPollingProperties longPollingProperties;
    private InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer;
    private LongPollingWorker longPollingWorker;


    public GrayEventLongPollingReceiver(
            LongPollingProperties longPollingProperties,
            GrayEventPublisher grayEventPublisher,
            GrayEventRemoteClient grayEventRemoteClient,
            InstanceLocalInfoInitiralizer instanceLocalInfoInitiralizer) {
        this.grayEventPublisher = grayEventPublisher;
        this.grayEventRemoteClient = grayEventRemoteClient;
        this.longPollingProperties = longPollingProperties;
        this.instanceLocalInfoInitiralizer = instanceLocalInfoInitiralizer;

    }

    @Override
    public void start(long sortMark) {
        this.currentSortMark = sortMark;
        longPollingWorker = new LongPollingWorker(
                longPollingProperties, grayEventRemoteClient, this, instanceLocalInfoInitiralizer);
    }


    @Override
    public void shutdown() {
        longPollingWorker.shutdown();
    }


    @Override

    public GrayEventPublisher getGrayEventPublisher() {
        return grayEventPublisher;
    }

    @Override
    public long getLocationNewestSortMark() {
        return currentSortMark;
    }


    @Override
    protected synchronized void updateLocationNewestSortMark(long sortMark) {
        if (sortMark > currentSortMark) {
            currentSortMark = sortMark;
        }
    }
}