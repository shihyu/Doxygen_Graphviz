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
				namespace example2
				{
					///
					/// <summary> * �ӿڵľ���ʵ�ֶ���B  </summary>
					/// 
					class ImplB : public Api
					{
					public:
						virtual void operation(std::string s);
					};

				}
			}
		}
	}
}