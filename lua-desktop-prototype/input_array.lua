require "class"
require "utils"

input_array = class:new()

local INPUT_ARRAY_WIDTH_Q = 0.8
local INPUT_ARRAY_HEIGHT_Q = 0.225
local KEY_OFFSET_WIDTH_Q = 0.03
local MAX_KEYS_PER_WIDTH = 8
local MAX_LINES_PER_HEIGHT = 3

function input_array:init(cam, word)
	self:setup_key_proportions(cam)
	self:setup_lines(word)
end

function input_array:setup_key_proportions(cam)
	self.array_width = INPUT_ARRAY_WIDTH_Q * cam.virtual_w
	self.array_height = INPUT_ARRAY_HEIGHT_Q * cam.virtual_h
	self.key_width = self.array_width / MAX_KEYS_PER_WIDTH
	self.key_height = self.array_height / MAX_LINES_PER_HEIGHT
end

function input_array:get_max_width()
	return self.array_width
end

function input_array:get_max_height()
	return self.array_height
end

function input_array:setup_lines(word)
	self.lines = {}
	self.line_offsets = {}
	
	local subwords = self:split_word(word)
	
	for i = 1, #subwords do
		local subword = subwords[i]
		local subword_len = utf8.len(subword.str)
		
		self.lines[i - 1] = {}
		self.line_offsets[i - 1] = (self.array_width - self.key_width * subword_len) / 2
		
		for j = 1, utf8.len(subword.str) do
			self.lines[i - 1][j - 1] = {
				letter = utf8.sub(subword.str, j, j),
				new_line_dash = subword.separator ~= " ",
				animation_type = nil,
				animation_timer = 0
			}
		end
	end
end

function input_array:split_word(word)
	local subwords = {}
	local subword_begin = 1
	
	for i = 1, utf8.len(word) do
		local c = utf8.sub(word, i, i)
		
		if c == " " 
			or c == "-" 
			or i - subword_begin == MAX_KEYS_PER_WIDTH - 1 
			or i == utf8.len(word) 
		then
			local offset = (c == " " or c == " ") and 1 or 0
			local subword = utf8.sub(word, subword_begin, i - offset)
			subwords[#subwords + 1] = {
				str = subword,
				separator = c
			}
			subword_begin = i + 1
		end
	end
	
	assert(#subwords <= MAX_LINES_PER_HEIGHT)
	
	return subwords
end

function input_array:update(dt)
end

function input_array:draw(cam, font)
	local font_h = font:getHeight()
	local width_offset = self.key_width * KEY_OFFSET_WIDTH_Q
	
	love.graphics.setColor(0, 0, 0, 1)
	
	for i = 0, #self.lines - 1 do
		local line = self.lines[i]
		local line_offset = self.line_offsets[i]
		
		for j = 0, #line - 1 do
			local key = line[j]
			local x = line_offset + j * self.key_width + width_offset / 2
			local y = (i + 1) * self.key_height
			
			love.graphics.line(x, y, x + self.key_width, y)
		end
	end
end