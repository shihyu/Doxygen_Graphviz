#pragma once

#include "Api.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example4
				{
					///
					/// <summary> * ��ĳ���ӿڵ�һ��ʵ��  </summary>
					/// 
					class Impl : public Api
					{

					public:
						virtual void test1(std::string s);
					};

				}
			}
		}
	}
}