package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;

import java.util.function.BiFunction;

public enum GroupFunctionParameterType {
    COLUMN((ColumnElement element, MQLDataStorage mqlDataStorage) -> {

        return new MQLDataStorage(mqlDataStorage.getMqlDataSource(), mqlDataStorage.getMqlTable());
    }),
    SINGLE_ROW_FUNCTION((SingleRowFunctionElement element, MQLDataStorage mqlDataStorage) -> {
        return new MQLDataStorage(mqlDataStorage.getMqlDataSource(), mqlDataStorage.getMqlTable());
    }),
    VALUE((ValueElement element, MQLDataStorage mqlDataStorage) -> {
        return new MQLDataStorage(mqlDataStorage.getMqlDataSource(), mqlDataStorage.getMqlTable());
    });

    private BiFunction<MQLElement, MQLDataStorage, MQLDataStorage> operation;

    GroupFunctionParameterType(BiFunction<MQLElement, MQLDataStorage, MQLDataStorage> operation) {
        this.operation = operation;
    }

    public Object operateWith(MQLElement parameter, MQLDataStorage mqlDataStorage) {

        operation.apply(parameter, mqlDataStorage);
    }
}
