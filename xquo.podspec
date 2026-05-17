require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "xquo"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.license      = "MIT"
  s.homepage     = "https://github.com/ulisesmac/xquo"
  s.author       = "Ulises Manuel Cardenas"
  s.source       = { :git => "https://github.com/ulisesmac/xquo.git", :tag => "#{s.version}" }
  s.platforms    = { :ios => "15.1" }
  s.source_files = "ios/**/*.{h,m,mm,cpp}"
  s.requires_arc = true

  install_modules_dependencies(s)
end
