#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example10
				{

					class Singleton
					{
					///	
					///	 <summary> * �Ա���ʵ���ı������volatile������ </summary>
					///	 
					private:
						volatile static Singleton instance;
						Singleton();
					public:
						static Singleton getInstance();
					};


				}
			}
		}
	}
}