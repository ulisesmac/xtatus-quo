(ns utils.number-test
  (:require
    [cljs.test :refer [are deftest is testing]]
    [utils.money :as money]
    [utils.number]))

(deftest convert-to-whole-number-test
  (testing "correctly converts fractional amounts to whole numbers"
    (is (= "123.45" (utils.number/convert-to-whole-number 12345 2)))
    (is (= "1.2345" (utils.number/convert-to-whole-number 12345 4)))
    (is (= "12345" (utils.number/convert-to-whole-number 1234500 2)))
    (is (= "0.123" (utils.number/convert-to-whole-number 123 3)))
    (is (= "1000" (utils.number/convert-to-whole-number 1000000 3))))

  (testing "handles zero decimals"
    (is (= "12345" (utils.number/convert-to-whole-number 12345 0))))

  (testing "handles negative amounts"
    (is (= "-123.45" (utils.number/convert-to-whole-number -12345 2)))
    (is (= "-1.2345" (utils.number/convert-to-whole-number -12345 4)))
    (is (= "-0.123" (utils.number/convert-to-whole-number -123 3))))

  (testing "handles zero amount"
    (is (= "0" (utils.number/convert-to-whole-number 0 2)))
    (is (= "0" (utils.number/convert-to-whole-number 0 0))))

  (testing "handles BigNumber amounts with 18 decimals"
    (let [amount (money/bignumber "3141969777175276657")]
      (is (= "3.141969777175276657" (utils.number/convert-to-whole-number amount 18))))))

(deftest parse-int-test
  (testing "defaults to zero"
    (is (= 0 (utils.number/parse-int nil))))

  (testing "accepts any other default value"
    (is (= 3 (utils.number/parse-int "" 3)))
    (is (= :invalid-int (utils.number/parse-int "" :invalid-int))))

  (testing "valid numbers"
    (is (= -6 (utils.number/parse-int "-6a" 0)))
    (is (= 6 (utils.number/parse-int "6" 0)))
    (is (= 6 (utils.number/parse-int "6.99" 0)))
    (is (= -6 (utils.number/parse-int "-6" 0)))))

(deftest parse-float-test
  (testing "defaults to zero"
    (is (= 0 (utils.number/parse-float nil))))

  (testing "accepts any other default value"
    (is (= 3 (utils.number/parse-float "" 3)))
    (is (= :invalid-float (utils.number/parse-float "" :invalid-float))))

  (testing "valid numbers"
    (is (= -6 (utils.number/parse-float "-6a")))
    (is (= 6 (utils.number/parse-float "6")))
    (is (= 6.99 (utils.number/parse-float "6.99" 0)))
    (is (= -6.9 (utils.number/parse-float "-6.9" 0)))))

(deftest small-number-threshold-test
  (testing "correctly generates threshold strings based on decimal count"
    (is (= "<0.1" (utils.number/small-number-threshold 1)))
    (is (= "<0.01" (utils.number/small-number-threshold 2)))
    (is (= "<0.001" (utils.number/small-number-threshold 3)))
    (is (= "<0.000001" (utils.number/small-number-threshold 6)))
    (is (= "<0.0000000001" (utils.number/small-number-threshold 10)))
    (is (= "<0.000000000000000001" (utils.number/small-number-threshold 18)))
    (is (= "<0.000000000000000000001" (utils.number/small-number-threshold 21))))

  (testing "handles edge cases for decimal count"
    (is (= "0" (utils.number/small-number-threshold 0)))
    (is (= "0" (utils.number/small-number-threshold -1)))))

(deftest valid-decimal-count?-test
  (testing "valid decimal count check for numbers with varying decimals"
    (is (true? (utils.number/valid-decimal-count? 123 2)))
    (is (true? (utils.number/valid-decimal-count? 123 0)))

    (is (true? (utils.number/valid-decimal-count? 123.45 2)))
    (is (true? (utils.number/valid-decimal-count? 123.4 2)))
    (is (true? (utils.number/valid-decimal-count? 123.456 3)))

    (is (false? (utils.number/valid-decimal-count? 123.456 2)))
    (is (false? (utils.number/valid-decimal-count? 123.456789 4)))

    (is (true? (utils.number/valid-decimal-count? 123.0 1)))
    (is (true? (utils.number/valid-decimal-count? -123.45 2)))
    (is (false? (utils.number/valid-decimal-count? -123.4567 3)))

    (is (true? (utils.number/valid-decimal-count? 1234567890.12 2)))
    (is (false? (utils.number/valid-decimal-count? 1234567890.12345 3)))))

(deftest format-decimal-fixed-test
  (testing "Format decimal numbers correctly without rounding"
    (is (= "123.45" (utils.number/format-decimal-fixed "123.456" 2)))
    (is (= "123.456" (utils.number/format-decimal-fixed "123.456" 3)))

    (is (= "123.45600" (utils.number/format-decimal-fixed "123.456" 5)))
    (is (= "123.000" (utils.number/format-decimal-fixed "123.000" 3)))

    (is (= "123" (utils.number/format-decimal-fixed "123.999" 0)))

    (is (= "-123.4" (utils.number/format-decimal-fixed "-123.456" 1)))
    (is (= "-123.45" (utils.number/format-decimal-fixed "-123.456" 2)))

    (is (= "0.0006" (utils.number/format-decimal-fixed "0.000650462672354754" 4)))
    (is (= "999999999.999" (utils.number/format-decimal-fixed "999999999.999999" 3)))))

(deftest format-number-with-si-suffix-test
  (testing "formats larger numbers with SI suffixes correctly"
    (are [n expected] (= expected (utils.number/format-number-with-si-suffix n))
     1e6           "1M"
     12345678      "12.3M"
     1200000000    "1.2B"
     3500000000000 "3.5T"
     999999        "1M"
     1e12          "1T"
     999900000000  "999.9B"
     1.0e12        "1T"
     0             "0"
     10            "10"
     14199         "14.2K"
     500000        "500K"
     999990000     "1B"
     0.000999      "0.000999")))
