package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import org.assertj.core.api.SoftAssertions;

/**
 * *****************************************************************
 * Hochschule Muenchen Fakultaet 07 (Informatik)		**
 * Praktikum fuer Softwareentwicklung 1 IF1B  WS15/16	**
 * *****************************************************************
 * Autor: Sebastian Balz					**
 * Datum 13.10.2017											**
 * Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis                **
 * *****************************************************************
 * **
 * *****************************************************************
 */



public class IssueSoftAssert extends SoftAssertions{

    public IssueSoftAssert(){

    }
    public IssueAssert assertThat(Issue actual){
        return proxy(IssueAssert.class, Issue.class, actual);
    }

}
