#pragma once

#include "Api.h"
#include "Impl.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example3
				{
					///
					/// <summary> * �����࣬��������Api���� </summary>
					/// 
					class Factory
					{
					///	
					///	 <summary> * ����Ĵ���Api����ķ��� </summary>
					///	 * <returns> ����õ�Api���� </returns>
					///	 
					public:
						static Api *createApi();
					};

				}
			}
		}
	}
}