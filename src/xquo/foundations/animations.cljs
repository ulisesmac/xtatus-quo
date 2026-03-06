(ns xquo.foundations.animations)

(def common-values
  {:press-feedback {:default {:transform                  [{:scale 1}
                                                          {:translate-y 0}]
                              :transition-property        "transform"
                              :transition-duration        "220ms"
                              :transition-timing-function "ease"}
                    :pressed {:transform                  [{:scale 0.985}
                                                          {:translate-y 2}]
                              :transition-property        "transform"
                              :transition-duration        "120ms"
                              :transition-timing-function "ease-out"}}
   :state-change   {:transition-property        "transform"
                    :transition-timing-function "ease-in-out"
                    :toggle-duration            "300ms"
                    :radio-expand-duration      "300ms"
                    :radio-collapse-duration    "200ms"}})
