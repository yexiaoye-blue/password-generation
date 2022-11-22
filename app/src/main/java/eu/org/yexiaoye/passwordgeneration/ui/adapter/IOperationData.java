package eu.org.yexiaoye.passwordgeneration.ui.adapter;

/**
 * 用于Adapter与ItemTouchHelper的数据统一
 */
public interface IOperationData {

    /**
     * 数据交换位置的方法
     * @param fromPosition  源位置
     * @param destPosition  目标位置
     */
    void onItemMove(int fromPosition, int destPosition);

    /**
     * 数据删除
     * @param position  要删除item的postion
     */
    void onItemRemove(int position);
}
