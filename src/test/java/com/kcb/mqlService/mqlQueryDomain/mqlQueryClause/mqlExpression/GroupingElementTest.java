package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.GroupFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.SUBSTR;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GroupByClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class GroupingElementTest {

    private MQLDataStorage mqlDataStorage;

    @Before
    public void makeMqlDataSource() {
        MQLDataSource mqlDataSource = new MQLDataSource();

        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));
        rawDataSource.put("D", TestDataFactory.tableOf("test"));
        rawDataSource.put("E", TestDataFactory.tableOf("products"));


        FromClause from = new FromClause();
        mqlDataStorage = from.makeMqlDataSources(rawDataSource);

        mqlDataStorage.setMqlTable(
                new MQLTable(
                        new HashSet<>(Collections.singletonList("E")),
                        mqlDataStorage.getMqlDataSource().dataSourceOf("E")
                )
        );
    }

    /**
     * SUM(E.PRICE)
     * GROUP BY E.SupplierID
     */
    @Test
    public void maxWithColumnParameterTest() {

        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement max = new MAX(new ColumnElement(functionParameter));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(max.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }


    /**
     * MAX (LENGTH(E.Price))
     *
     * GROUP BY E.SupplierID
     */

    @Test
    public void maxWithFunctionParameterTest() {

        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement max = new MAX(new ColumnElement(functionParameter));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(max.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());

    }

    /**
     * MIN (LENGTH(E.Price))
     *
     * GROUP BY E.SupplierID
     */

    @Test
    public void minWithFunctionParameterTest() {

        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement max = new MIN(new ColumnElement(functionParameter));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(max.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());

    }
    /**
     * COUNT(LENGTH(SUBSTR(E.ProductName)))
     * GROUP BY E.SupplierID
     */
    @Test
    public void countWithColumnParameterTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.ProductName";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement max = new COUNT(new LENGTH(new SUBSTR(new ColumnElement(functionParameter), new ValueElement(1), new ValueElement(6))));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(max.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }

    /**
     *
     * SUM(E.Price)
     * GROUP BY E.SupplierID
     */
    @Test
    public void sumWithColumnParameterTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement sum = new SUM(new ColumnElement(functionParameter));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(sum.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }

    /**
     *
     * SUM(3)
     * GROUP BY E.SupplierID
     */
    @Test
    public void sumWithValueParameterTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement sum = new SUM(new ValueElement(3));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(sum.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }


    /**
     *
     * SUM(LENGTH(SUBSTR(PRODUCTNAME, 1, 6)))
     * GROUP BY E.SupplierID
     */
    @Test
    public void sumWithFunctionParameterTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.ProductName";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement sum = new SUM(new LENGTH(new ValueElement(11)));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(sum.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            //eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }

    /**
     *
     * AVG(E.Price)
     * GROUP BY E.SupplierID
     */
    @Test
    public void avgWithColumnTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement avg = new AVG(new ColumnElement(functionParameter));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(avg.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            //eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }

    /**
     *
     * AVG(LENGTH(11))
     * GROUP BY E.SupplierID
     */
    @Test
    public void avgWithFunctionParameterTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement avg = new AVG(new LENGTH(new ValueElement(11)));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(avg.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            //eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }

    /**
     *
     * AVG(LENGTH(E.ProductName))
     * GROUP BY E.SupplierID
     */
    @Test
    public void avgWithFunctionParameterHasColumnTest() {
        String column1 = "E.SupplierID";
        String functionParameter = "E.ProductName";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));

        GroupByClause groupBy = new GroupByClause(groupingElements);
        MQLDataStorage result = groupBy.executeClause(mqlDataStorage);

        GroupFunctionElement avg = new AVG(new LENGTH(new ColumnElement(functionParameter)));
        List<BigDecimal> groupedMax = new ArrayList<>();

        int start = 0;
        for (int end : result.getMqlTable().getGroupingIdxs()) {
            groupedMax.add(new BigDecimal(String.valueOf(avg.executeAbout(start, end, result))));
            start = end + 1;
        }
        //groupedMax.sort(Comparator.comparing(BigDecimal::doubleValue));

        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            //eachRow.remove("E.ProductName");
        });

        print(result);
        System.out.println(groupedMax);
        System.out.println(groupedMax.size());
    }


    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
        System.out.println(mqlDataStorage.getMqlTable().getGroupingIdxs());
        System.out.println(mqlDataStorage.getMqlTable().getGroupingIdxs().size());
    }
}
