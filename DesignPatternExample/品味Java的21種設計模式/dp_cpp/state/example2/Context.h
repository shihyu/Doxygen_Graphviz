#pragma once

#include "State.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example2
				{


					///
					/// <summary> * ����ͻ�����Ȥ�Ľӿڣ�ͨ����ά��һ��State���͵Ķ���ʵ�� </summary>
					/// 
					class Context
					{
					///	
					///	 <summary> * ����һ��State���͵Ķ���ʵ�� </summary>
					///	 
					private:
						State *state;
					///	
					///	 <summary> * ����ʵ��State�Ķ����ʵ��  </summary>
					///	 * <param name="state"> ʵ��State�Ķ����ʵ��  </param>
					///	 
					public:
						virtual void setState(State *state);
					///	
					///	 <summary> * �û�����Ȥ�Ľӿڷ��� </summary>
					///	 * <param name="sampleParameter"> ʾ����� </param>
					///	 
						virtual void request(std::string sampleParameter);


					};
				}
			}
		}
	}
}