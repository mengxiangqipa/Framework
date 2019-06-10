package com.framework.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by  作者：Administrator on 2016/3/18 0018 11:00.
 * 消息轮询队列
 */
public class MessageQueueUtil {
    private static final String TAG = "MessageQueueUtil";
    private static volatile MessageQueueUtil messageQueueUtil;
    LinkedList<RunnableTAG> taskLinkedList;
    // 任务不能重复
    private Set<String> taskIdSet;



    private MessageQueueUtil() {
        taskLinkedList = new LinkedList<RunnableTAG>();
        taskIdSet = new HashSet<String>();
    }

    /**
     * 获取MessageQueueUtil单例
     *
     * @return
     */
    public static MessageQueueUtil getInstance() {
        if (messageQueueUtil == null) {
            synchronized (MessageQueueUtil.class) {
                if (messageQueueUtil == null) {
                    messageQueueUtil = new MessageQueueUtil();
                }
            }
        }
        return messageQueueUtil;
    }

    /**
     * 添加任务
     *
     * @param runnableTag
     */
    public void addRunnableTask(RunnableTAG runnableTag) {
        synchronized (taskLinkedList) {
            try {
                if (!hasTask(runnableTag.getTag())) {
                    // 增加task任务
                    taskIdSet.add(TAG);
                    taskLinkedList.addLast(runnableTag);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 移除任务
     *
     * @param TAG
     */
    public void removeRunnableTask(String TAG) {
        synchronized (taskLinkedList) {
            try {
                LinkedList<RunnableTAG> taskList = getTaskList();
                if (null != taskList && taskList.size() > 0) {
                    int len = taskList.size();
                    for (int j = 0; j < len; j++) {
                        RunnableTAG runnableTAG = taskList.get(j);
                        if (runnableTAG.getTag().equals(TAG)) {
                            taskList.remove(runnableTAG);
                            taskIdSet.remove(TAG);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void removeFirstTask() {
        try {
            synchronized (taskLinkedList) {
                taskLinkedList.removeFirst();
                taskIdSet.remove(0);
            }
        } catch (Exception e) {
        }
    }

    public boolean hasTask(String TAG) {
        synchronized (taskIdSet) {
            try {
                if (taskIdSet.contains(TAG)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    public LinkedList<RunnableTAG> getTaskList() {
        synchronized (taskLinkedList) {
            try {
                return taskLinkedList;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    public RunnableTAG getTopTask() {
        synchronized (taskLinkedList) {
            try {
                if (taskLinkedList.size() > 0) {
                    System.out.println("下载管理器增加下载任务：" + "取出任务");
                    RunnableTAG downloadTask = taskLinkedList.removeFirst();
                    taskIdSet.remove(0);
                    return downloadTask;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    class RunnableTAG implements Runnable {
        String Tag;

        public RunnableTAG(String Tag) {
            this.Tag = Tag;
        }

        public String getTag() {
            return Tag;
        }

        @Override
        public void run() {

        }
    }
}
