package com.user;

import com.google.common.collect.Sets;

import com.netflix.servo.util.Strings;
import com.opencsv.CSVWriter;
import com.oracle.tools.packager.IOUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class DataHandler {

  private static final String FILE_PATH = "";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private String ecid = "1";

  final private Map<String, Set<String>> params = new HashMap<>();

  private Map<String, NamedParameterJdbcTemplate> namedParameterJdbcTemplateMap = new HashMap<>();


  public void query() {
    params.put("ecid", Sets.newHashSet(ecid));
    beforeQuery();
    if (Strings.isNullOrEmpty(ecid)) {
      throw new RuntimeException("Ecid should not to be empty");
    }
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    saveParamSQLResult(namedParameterJdbcTemplate);
    saveFileSQLResult(namedParameterJdbcTemplate);
  }

  private void beforeQuery() {
    Set<String> ecids = params.get("ecid");
    ecids.forEach(t -> {
      try {
        Path pathToFile = Paths.get(params.get("ecid").iterator().next() + "/");
        Files.createDirectories(pathToFile);
        Files.walk(pathToFile)
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (Exception err) {
        err.printStackTrace();
        throw new RuntimeException();
      }
    });
  }

  private void saveParamSQLResult(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    try {
      SqlConfig sqlConfig = new SqlConfig();
      sqlConfig.setParamSQL(true);
      sqlConfig.setSaveAsCsv(false);
      Map<String, String> propertiesMap = readSQLProperties(sqlConfig);
      propertiesMap.forEach((k, sql) -> {
        sqlConfig.setParamName(StringUtils.split(k, "_")[1]);
        if (params.get(sqlConfig.getParamName()) != null) {
          throw new RuntimeException("Param have existed. " + sqlConfig.getParamName());
        } else {
          params.put(sqlConfig.getParamName(), null);
        }
        namedParameterJdbcTemplate.query(sql, params, new CustomRowCallbackHandler(sqlConfig));
      });
      saveParamToFile();
    } catch (Exception err) {
      err.printStackTrace();
    }
  }


  private void saveFileSQLResult(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    try {
      SqlConfig sqlConfig = new SqlConfig();
      sqlConfig.setParamSQL(false);
      sqlConfig.setSaveAsCsv(true);
      Map<String, String> propertiesMap = readSQLProperties(sqlConfig);
      propertiesMap.forEach((k, sql) -> {
        sqlConfig.setFileName(k);
        namedParameterJdbcTemplate.query(sql, params, new CustomRowCallbackHandler(sqlConfig));
      });
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public class CustomRowCallbackHandler implements RowCallbackHandler {
    private SqlConfig sqlConfig;

    public CustomRowCallbackHandler(SqlConfig sqlConfig) {
      this.sqlConfig = sqlConfig;
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {
      try {
        if (sqlConfig.isParamSQL()) {
          Set<String> param = Sets.newHashSet();
          do {
            int count = rs.getMetaData().getColumnCount();
            if (count > 1 || count == 0) {
              throw new RuntimeException("Param SQL. Column should only be 1.");
            }
            param.add(rs.getString(1));
            params.put(sqlConfig.getParamName(), param);
          } while (rs.next());
        }
        if (sqlConfig.isSaveAsCsv()) {
          Path pathToFile =
              Paths.get(params.get("ecid").iterator().next() + "/" + sqlConfig.getFileName());
          CSVWriter w = new CSVWriter(new FileWriter(pathToFile.toFile()), ',');
          int columnCount = rs.getMetaData().getColumnCount();
          //Write column names
          String[] ssn = new String[columnCount];
          for (int i = 1; i <= columnCount; i++) {
            ssn[i - 1] = rs.getMetaData().getColumnName(i);
          }
          w.writeNext(ssn);
          //Write data
          do {
            String[] ss = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
              if (rs.getMetaData().getColumnType(i) == Types.BLOB) {
                continue;
              }
              ss[i - 1] = rs.getString(i);
            }
            w.writeNext(ss);

          } while (rs.next());
          w.flush();
          w.close();
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private Map readSQLProperties(SqlConfig sqlConfig) {
    Map<String, String> propertiesMap = new TreeMap<>();
    Properties properties = new Properties();
    String propertyFileName;
    if (sqlConfig.isParamSQL()) {
      propertyFileName = "sql.properties";
    } else {
      propertyFileName = "fileSql.properties";
      //return readSQL();
    }
    try {
      properties.load(getClass().getClassLoader().getResourceAsStream(propertyFileName));
      propertiesMap.putAll(properties.entrySet()
          .stream()
          .collect(Collectors.toMap(e -> e.getKey().toString(),
              e -> e.getValue().toString())));
    } catch (Exception err) {
      err.printStackTrace();
    }
    return propertiesMap;
  }

  private Map readSQL() {
    Map<String, String> propertiesMap = new TreeMap<>();
    try {
      int i = 1;
      URL url = getClass().getClassLoader().getResource("a.SQL");
      List<String> list = Files.readAllLines(Paths.get(url.toURI()));
      StringBuilder stringBuilder = new StringBuilder();
      for (String t : list) {
        if (!t.isEmpty()) {
          stringBuilder.append(t + " ");
          if (t.endsWith(";")) {
            propertiesMap.put("File_" + (i++), stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length() - 1);
          }
        }
      }
    } catch (Exception err) {
      err.printStackTrace();
    }
    return propertiesMap;
  }


  private void saveParamToFile() {

    try (FileWriter fileWriter = new FileWriter("ParamSQLResult")) {
      params.forEach((k, v) -> {
        try {
          fileWriter.write(k + ": " + (v == null ? "" : Strings.join(",", v.iterator())));
          fileWriter.write("\r\n");
        } catch (Exception err) {
          err.printStackTrace();
        }
      });
      fileWriter.flush();
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  private void extractSQL(String file) {
    Map<String, String> propertiesMap = new TreeMap<>();
    Properties properties = new Properties();
    try {
      FileWriter fileWriter = new FileWriter(file.replace(".SQL", "") + "_Extract.SQL");
      properties.load(getClass().getClassLoader().getResourceAsStream(file));
      propertiesMap.putAll(properties.entrySet()
          .stream()
          .collect(Collectors.toMap(e -> e.getKey().toString(),
              e -> e.getValue().toString())));
      propertiesMap.forEach((k, v) -> {
        try {
          fileWriter.write(v);
          fileWriter.write("\r\n");
        } catch (Exception err) {
          err.printStackTrace();
        }
      });
      fileWriter.flush();
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public Map<Character, Set<String>> groupSQLBySchema() {
    Map<Character, Set<String>> groupSQL = new HashMap<>();
    try {
      Properties properties = new Properties();
      properties.load(getClass().getClassLoader().getResourceAsStream("deleteSQL.SQL"));
      properties.entrySet().forEach(e -> {
        char schema = (e.getKey().toString()).charAt(0);
        Set<String> sqls = groupSQL.getOrDefault(schema, Sets.newHashSet());
        sqls.add(e.getValue().toString());
        groupSQL.put(schema, sqls);
      });
    } catch (Exception err) {
      err.printStackTrace();
    }
    return groupSQL;
  }

  public NamedParameterJdbcTemplate createTemplate(String schema,
                                                   String url,
                                                   String username,
                                                   String password) {
    if (Strings.isNullOrEmpty(schema)){
      schema = "NO_SCHEMA";
    }
    if (namedParameterJdbcTemplateMap.containsKey(schema) && !Strings.isNullOrEmpty(schema)){
      return namedParameterJdbcTemplateMap.get(schema);
    }
    org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
    dataSource.setUrl(url);
    if (!"NO_SCHEMA".equals(schema)){
      dataSource.setUsername(username + "[" + schema + "]");
    } else {
      dataSource.setUsername(username);
    }
    dataSource.setPassword(password);
    JdbcTemplate jdbcTemplate =  new JdbcTemplate(dataSource);
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    namedParameterJdbcTemplateMap.put(schema, namedParameterJdbcTemplate);
    return namedParameterJdbcTemplate;
  }

  public static void main(String[] args) {
    new DataHandler().createTemplate(null, "jdbc:mysql://localhost:3306/test", "root", "12345678");
  }
}
