#pragma once

#include "CookApi.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example5
				{
					///
					/// <summary> * ��ʦ�������Ȳ� </summary>
					/// 
					class HotCook : public CookApi
					{

					public:
						virtual void cook(std::string name);
					};
				}
			}
		}
	}
}