package com.zfy.lifecyclex;

import com.zfy.lifecyclex.fragment.LifecycleXHost;

/**
 * CreateAt : 2018/7/20
 * Describe : 一个可执行的操作
 *
 * @author chendong
 */
public interface Executable {
    void execute(LifecycleXHost fragment);
}
