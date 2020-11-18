package com.cerebra.fileMerger.util;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("pathConstants")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PathConstants {
    public static String lastChosenFilePath = null;
}