require "utils"

KEYSET = {
	czech = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
	german = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß"
}

function load_dictionary(filename, name, input_language, output_language)
	assert(KEYSET[input_language] ~= nil, input_language .. " not found!")
	assert(KEYSET[output_language] ~= nil, output_language .. " not found!")
	assert(love.filesystem.getInfo(filename) ~= nil, filename .. " does not exists!")
	
	local lines = file_read(filename)
	local words = {}
	
	for i=1, #lines do
		local line = lines[i]
		local sep = string.find(line, ",")
		
		assert(sep ~= nil, "Invalid line no: " .. i .. " in filename: " .. filename)
		
		words[#words + 1] = {
			[1] = utf8.sub(line, 1, sep - 1),
			[2] = utf8.sub(line, sep + 1, utf8.len(line))
		}
	end
	
	return {
		name = name,
		input_language = input_language,
		output_language = output_language,
		words = words
	}
end