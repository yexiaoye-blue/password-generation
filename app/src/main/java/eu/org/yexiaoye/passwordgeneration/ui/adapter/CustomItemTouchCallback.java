package eu.org.yexiaoye.passwordgeneration.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton$InspectionCompanion;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import eu.org.yexiaoye.passwordgeneration.R;
import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;

/**
 * ItemTouchHelper.Callback实现类，用来控制RecyclerView的item的拖拽与侧滑删除
 */
public class CustomItemTouchCallback extends ItemTouchHelper.Callback {

    // 这里的类型，将来可以抽成BaseAdapter
    private final PasswordListAdapter mAdapter;
    private List<PasswordInfo> mPasswordInfoList;

    public CustomItemTouchCallback(PasswordListAdapter adapter, List<PasswordInfo> passwordInfoList) {
        this.mAdapter = adapter;
        this.mPasswordInfoList = passwordInfoList;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 控制托拖拽的方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 控制滑动的方向（一般是左右）
        // int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.LEFT;
        // 返回方向值
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 获取被拖拽item的position
        int fromPosition = viewHolder.getAdapterPosition();
        // 获取目标item的position
        int destPosition = target.getAdapterPosition();
        // 若位置发生改变再交换位置
        if (fromPosition != destPosition) {
            mAdapter.onItemMove(fromPosition,destPosition);
        }

        // true:允许拖拽，反之则禁止拖拽
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 滑动处理
        /*int position = viewHolder.getAdapterPosition();
        mAdapter.onItemRemove(position);*/
    }


    // 是否允许滑动,默认值true
    @Override
    public boolean isItemViewSwipeEnabled() {
        // 禁止滑动
        return false;
    }

    /**
     * 在item底部绘制图形
     * @param c                     canvas:画布，可以用于绘制图形
     * @param recyclerView          recyclerView
     * @param viewHolder            被操作的item
     * @param dX                    item被操作时的X轴方向的偏移量
     * @param dY                    item被操作时的Y轴方向的偏移量
     * @param actionState           移动状态：无操作状态、拖拽状态、滑动状态
     * @param isCurrentlyActive     滑动分为两类：手指滑动和item位移动画滑动
     *                              isCurrentlyActive可以用于判断是哪种滑动，如果isCurrentlyActive为true则表示手指滑动
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        // 仅对侧滑状态下的效果做出改变
        /*if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 如果dx小于等于删除方块的宽度，就把方块滑出来
            if (Math.abs(dX) <= getSwipeLimitation(viewHolder)) {
                viewHolder.itemView.scrollTo(-(int) dX,0);
            }else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }*/

    }

    /**
     * 获取删除块的宽度
     * @param holder holder
     * @return
     */
    public int getSwipeLimitation(RecyclerView.ViewHolder holder) {
        ViewGroup viewGroup = (ViewGroup) holder.itemView;
        return viewGroup.getChildAt(1).getLayoutParams().width;
    }

    // clearView：清空View的状态；
    // 当拖拽或滑动动画结束时，也就是手指松开并等待动画结束时会执行clearView方法，这个方法可以用来初始化数据
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 当RecyclerView滑动的百分比大于该方法的返回值（阈值），那么就会执行上面的onSwipe()方法
     * @param viewHolder    正在滑动的viewHolder(也就是itemView)
     * @return              滑动阈值
     */
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeThreshold(viewHolder);
    }

    /**
     * Defines the maximum velocity ItemTouchHelper will ever calculate for pointer movements.
     * 定义ItemTouchHelper为指针的计算滑动最大速度阈值
     * @param defaultValue
     * @return
     */
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return super.getSwipeVelocityThreshold(defaultValue);
    }

    /**
     * Defines the minimum velocity which will be considered as a swipe action by the user.
     * 定义将被作为用户滑动动作的最小速度
     * @param defaultValue  The default value (in pixels per second) used by the ItemTouchHelper.
     * @return              最小滑动阈值
     */
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        // defaultValue默认值为800dp，
        return super.getSwipeEscapeVelocity(defaultValue);
    }


    // 返回值默认50%,拖拽距离百分比，也就是拖多远，调用onMove()方法
    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getMoveThreshold(viewHolder);
    }


    // 是否允许长按拖拽，默认值为true
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }



    // 拖拽滑动时调用
    // Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed.
    // 从静止状态变为拖拽或滑动的时候会回调该方法，参数actionState表示当前的状态
    // 这个方法可用于处理在拖拽或滑动时的UI变化
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    // 设置动画事件
    // 当手指松开时，item会移动到某个固定的位置这个动作就是item的动画，
    // 而getAnimationDuration方法可以设置动画的事件，默认为200ms或250ms
    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }
}
