package com.chrisdoyle.validation.tests;

import com.chrisdoyle.validation.Main;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 9/4/16.
 */
public class TestPostPublicationNoSig {

    @Test
    public void checkNexusPublications() throws Exception {
        new TestPostPublication().checkNexusPublicationsWithSignatures(false);

    }
}
