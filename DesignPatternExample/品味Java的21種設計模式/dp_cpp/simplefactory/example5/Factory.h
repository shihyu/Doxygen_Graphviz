#pragma once

#include "Api.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example5
				{
					///
					/// <summary> * �����࣬��������Api���� </summary>
					/// 
					class Factory
					{
					///	
					///	 <summary> * ����Ĵ���Api�ķ��������������ļ��Ĳ����������ӿ� </summary>
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