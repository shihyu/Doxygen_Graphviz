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
					/// <summary> * ��ʦ���������� </summary>
					/// 
					class CoolCook : public CookApi
					{
					public:
						virtual void cook(std::string name);
					};
				}
			}
		}
	}
}