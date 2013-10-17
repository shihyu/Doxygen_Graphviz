#pragma once

#include "MainboardApi.h"
#include "GAMainboard.h"
#include "MSIMainboard.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example1
				{
					///
					/// <summary> * ��������ļ򵥹��� </summary>
					/// 
					class MainboardFactory
					{
					///	
					///	 <summary> * ��������ӿڶ���ķ��� </summary>
					///	 * <param name="type"> ѡ���������͵Ĳ��� </param>
					///	 * <returns> ����ӿڶ���ķ��� </returns>
					///	 
					public:
						static MainboardApi *createMainboardApi(int type);
					};

				}
			}
		}
	}
}