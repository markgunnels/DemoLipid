(ns informatic_tools.config)

(def *db*
     {:classname   "com.mysql.jdbc.Driver"
      :subprotocol "mysql"
      :user         "root"
      :password     ""
      :subname      "//localhost:3306/lipid_informatics"})

(def *project-couch*
     "http://sctrdev2-v.mdc.musc.edu:5984/projects")

(def SITE-TITLE "INFORMATICS TOOLS")
(def SITE-URL "http://localhost:3000")
(def SITE-DESCRIPTION
     "Provides informatics tools for working with Lipidomics data.")

(def SAMPLE-IDENTIFIER-ATTRIBUTE-NAME "Sample ID")