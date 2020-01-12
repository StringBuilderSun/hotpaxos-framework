package com.hotpaxos.framework.common.core.invoke;

/**
 * 拦截执行器 可根据Channel 打开或者关闭 确定执行器执行时机
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public abstract class HotPaxInvoke implements Invoke, Comparable<HotPaxInvoke> {

    /**
     * 用于识别Channel当前状态  打开 或者  关闭
     *
     * @return 返回不可重复数组
     */
    public abstract String[] invokeType();

    /**
     * 排序序号
     *
     * @return
     */
   public abstract int invokeOrder();

    //默认全部是静态执行器， 可以通过重写该方法将属性变为true
    public boolean isStaicInvoke() {
        return true;
    }

    @Override
    public int compareTo(HotPaxInvoke o) {
        //order越小 优先级越高
        return Integer.compare(this.invokeOrder(), o.invokeOrder());
    }


}
