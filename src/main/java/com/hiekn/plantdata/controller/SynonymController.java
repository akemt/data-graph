package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 同义词控制层
 */
@RestController
@RequestMapping(value = "syn/synonyms")
public class SynonymController {

    @Autowired
    private SynonymService synonymService;




}
